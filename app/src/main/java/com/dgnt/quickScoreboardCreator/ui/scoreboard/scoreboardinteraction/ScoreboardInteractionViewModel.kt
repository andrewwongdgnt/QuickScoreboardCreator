package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.common.mapper.Mapper
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.domain.history.usecase.InsertHistoryUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.SCOREBOARD_IDENTIFIER
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.composable.Label
import com.dgnt.quickScoreboardCreator.ui.common.resourcemapping.soundEffectRes
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedIntervalData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedTeamData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay.TeamDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class ScoreboardInteractionViewModel @Inject constructor(
    private val resources: Resources,
    private val getScoreboardUseCase: GetScoreboardUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val insertHistoryUseCase: InsertHistoryUseCase,
    private val scoreboardLoader: ScoreboardLoader,
    private val scoreboardManager: ScoreboardManager,
    private val timeTransformer: TimeTransformer,
    private val historyScoreboardDataMapper: Mapper<HistoricalScoreboard, HistoricalScoreboardData>,
    savedStateHandle: SavedStateHandle,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {

    /**
     * Primary Display score
     */
    private val _primaryDisplayedScoreInfo = MutableStateFlow(DisplayedScoreInfo(listOf(), DisplayedScore.Blank))
    val primaryDisplayedScoreInfo: StateFlow<DisplayedScoreInfo> = _primaryDisplayedScoreInfo.asStateFlow()
    private val primaryScoresUpdateListener: (DisplayedScoreInfo) -> Unit = {
        _primaryDisplayedScoreInfo.value = it
    }

    /**
     * Secondary Display score
     */
    private val _secondaryDisplayedScoreInfo = MutableStateFlow(DisplayedScoreInfo(listOf(), DisplayedScore.Blank))
    val secondaryDisplayedScoreInfo: StateFlow<DisplayedScoreInfo> = _secondaryDisplayedScoreInfo.asStateFlow()
    private val secondaryScoresUpdateListener: (DisplayedScoreInfo) -> Unit = {
        _secondaryDisplayedScoreInfo.value = it
    }

    /**
     * Primary Display increment list
     */
    private val _primaryIncrementList = MutableStateFlow(emptyList<List<Int>>())
    val primaryIncrementList: StateFlow<List<List<Int>>> = _primaryIncrementList.asStateFlow()
    private val primaryIncrementListUpdateListener: (List<List<Int>>) -> Unit = {
        _primaryIncrementList.value = it
    }

    /**
     * Secondary Display increment list
     */
    private val _secondaryIncrementList = MutableStateFlow(emptyList<List<Int>>())
    val secondaryIncrementList: StateFlow<List<List<Int>>> = _secondaryIncrementList.asStateFlow()
    private val secondaryIncrementListUpdateListener: (List<List<Int>>) -> Unit = {
        _secondaryIncrementList.value = it
    }

    /**
     * Display interval number
     */
    private val _currentInterval = MutableStateFlow(1)
    val currentInterval: StateFlow<Int> = _currentInterval.asStateFlow()
    private val intervalIndexUpdateListener: (Int) -> Unit = {
        _currentInterval.value = it + 1
        secondaryScoreLabelList.getOrNull(it)?.let { secondaryScoreLabelInfo ->
            _secondaryScoreLabel.value = secondaryScoreLabelInfo
        }
        onTimerPause(true)
    }

    /**
     * Display team list
     */
    private val _teamList = MutableStateFlow(emptyList<TeamDisplay>())
    val teamList: StateFlow<List<TeamDisplay>> = _teamList.asStateFlow()
    private val teamSizeUpdateListener: (Int) -> Unit = {
        _teamList.value = (0 until it).map { index ->
            teamList.value.getOrNull(index) ?: TeamDisplay.UnSelected
        }
    }

    /**
     * Display time
     */
    private val _timeData = MutableStateFlow(TimeData(0, 0, 0))
    val timeData: StateFlow<TimeData> = _timeData.asStateFlow()
    private val timeUpdateListener: (Long) -> Unit = {
        _timeData.value = timeTransformer.toTimeData(it)
        if (it <= 0L) {
            stopTimerJob()
        }
    }

    private var scoreboardType: ScoreboardType? = null

    /**
     * Winners by team index
     */
    private val winnersUpdateListener: (Set<Int>) -> Unit = {
        //TODO handle winners

        stopTimerJob()
        isHistoryTemporary = false
        viewModelScope.launch {
            historyEntityId = insertHistory()
        }
    }

    private val intervalOnEndListener: (IntervalEndSound) -> Unit = { sound ->
        sound.soundEffectRes()?.let {
            sendUiEvent(UiEvent.PlaySound(it))
        }
    }

    private val _simpleMode = MutableStateFlow(true)
    val simpleMode: StateFlow<Boolean> = _simpleMode.asStateFlow()

    private val _timerInProgress = MutableStateFlow(false)
    val timerInProgress: StateFlow<Boolean> = _timerInProgress.asStateFlow()

    private var timerJob: Job? = null

    private var scoreboardIdentifier: ScoreboardIdentifier? = null

    private var timelineViewerTitle = ""
    private var timelineViewerIcon = ScoreboardIcon.BASKETBALL

    private var historyEntityId: Int? = null
    private var isHistoryTemporary = true

    private val _intervalLabel = MutableStateFlow<Label>(Label.Custom(""))
    val intervalLabel = _intervalLabel.asStateFlow()

    private val _secondaryScoreLabel = MutableStateFlow<Label>(Label.Custom(""))
    val secondaryScoreLabel = _secondaryScoreLabel.asStateFlow()

    private var secondaryScoreLabelList = listOf<Label>()

    init {
        savedStateHandle.get<ScoreboardIdentifier>(SCOREBOARD_IDENTIFIER)?.let { sId ->
            when (sId) {
                is ScoreboardIdentifier.Custom -> initWithId(sId.id)
                is ScoreboardIdentifier.Default -> initWithScoreboardType(sId.scoreboardType)
            }
            scoreboardIdentifier = sId
        }

        scoreboardManager.primaryScoresUpdateListener = primaryScoresUpdateListener
        scoreboardManager.secondaryScoresUpdateListener = secondaryScoresUpdateListener
        scoreboardManager.timeUpdateListener = timeUpdateListener
        scoreboardManager.intervalIndexUpdateListener = intervalIndexUpdateListener
        scoreboardManager.primaryIncrementListUpdateListener = primaryIncrementListUpdateListener
        scoreboardManager.secondaryIncrementListUpdateListener = secondaryIncrementListUpdateListener
        scoreboardManager.teamSizeUpdateListener = teamSizeUpdateListener
        scoreboardManager.winnersUpdateListener = winnersUpdateListener
        scoreboardManager.intervalOnEndListener = intervalOnEndListener

        scoreboardManager.triggerUpdateListeners()
    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getScoreboardUseCase(id)?.let {
                timelineViewerTitle = it.title
                timelineViewerIcon = it.icon
            }
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {
        this.scoreboardType = scoreboardType
        _intervalLabel.value = Label.Resource(scoreboardType.intervalLabelRes)
        timelineViewerTitle = resources.getString(scoreboardType.titleRes)
        timelineViewerIcon = scoreboardType.icon
        scoreboardType.rawRes.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let { defaultScoreboardConfig ->
            scoreboardManager.apply {
                winRule = defaultScoreboardConfig.winRuleType.toWinRule()
                intervalList = defaultScoreboardConfig.intervalList.map {
                    it.scoreInfo.toScoreInfo() to it.intervalData.toIntervalData()
                }
            }
            secondaryScoreLabelList = defaultScoreboardConfig.intervalList.map {
                Label.Resource(scoreboardType.secondaryScoreLabelRes)
            }
        }

    }

    fun onScoreChange(
        isPrimary: Boolean,
        scoreIndex: Int,
        incrementIndex: Int,
        main: Boolean,
    ) = scoreboardManager.updateScore(isPrimary, scoreIndex, incrementIndex, main)

    fun toTeamPicker(index: Int) = sendUiEvent(UiEvent.TeamPicker(index))

    fun onTeamPick(updatedTeamData: UpdatedTeamData) = viewModelScope.launch {
        _teamList.value = (0 until scoreboardManager.currentTeamSize).mapNotNull { index ->
            if (updatedTeamData.scoreIndex == index) {
                getTeamUseCase(updatedTeamData.teamId)?.let {
                    TeamDisplay.Selected(it.title, it.icon)
                }
            } else {
                teamList.value.getOrNull(index)
            }
        }
    }

    fun onTimerPause(reset: Boolean) {
        stopTimerJob()
        if (reset) {
            scoreboardManager.resetTime()
        }
    }

    fun onTimerStart() {
        stopTimerJob()
        if (!scoreboardManager.canTimeAdvance()) {
            _timerInProgress.value = false
            return
        }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(100)
                scoreboardManager.updateTimeBy(100L)

            }
        }
        _timerInProgress.value = true

    }

    fun onToggleModeChange(isSimpleMode: Boolean) {
        _simpleMode.value = !isSimpleMode
    }

    fun toIntervalEditor() {
        stopTimerJob()
        scoreboardIdentifier?.let { sId ->
            sendUiEvent(UiEvent.IntervalEditor(timeTransformer.fromTimeData(timeData.value), currentInterval.value - 1, sId))
        }
    }

    fun onIntervalEdit(updatedIntervalData: UpdatedIntervalData) {
        updatedIntervalData.takeUnless { it.timeValue < 0 || it.intervalIndex < 0 }?.let {
            stopTimerJob()
            scoreboardManager.updateInterval(it.intervalIndex)
            scoreboardManager.updateTime(it.timeValue)

        }
    }

    fun toTimelineViewer() = viewModelScope.launch {
        insertHistory().let {
            historyEntityId = it
            sendUiEvent(UiEvent.TimelineViewer(it, currentInterval.value - 1))
        }
    }

    private suspend fun insertHistory(): Int {
        val intervalLabel = when (val l = intervalLabel.value) {
            is Label.Custom -> IntervalLabel.Custom(l.value)
            else -> scoreboardType?.let {
                IntervalLabel.ScoreboardType(it)
            } ?: IntervalLabel.Custom("")
        }
        val teamList = teamList.value.map {
            when (it) {
                is TeamDisplay.UnSelected -> TeamLabel.None
                is TeamDisplay.Selected -> TeamLabel.Custom(it.name, it.icon)
            }
        }
        val historicalScoreboard = scoreboardManager.createTimeline(intervalLabel, teamList)

        return insertHistoryUseCase(
            HistoryEntity(
                id = historyEntityId,
                title = timelineViewerTitle,
                description = "",
                icon = timelineViewerIcon,
                lastModified = DateTime.now(),
                historicalScoreboard = historyScoreboardDataMapper.map(historicalScoreboard),
                temporary = isHistoryTemporary
            )
        ).toInt()
    }

    override fun onCleared() {
        super.onCleared()
        stopTimerJob()
    }

    private fun stopTimerJob() {
        timerJob?.cancel()
        _timerInProgress.value = false
    }
}