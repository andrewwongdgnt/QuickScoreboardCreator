package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboard

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.SPORT_IDENTIFIER
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.PlaySound
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TeamPicker
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TimelineViewer
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.manager.ScoreboardManager
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.GetSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.TimeConversionUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.intervalLabelRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.rawRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.secondaryScoreLabelRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.soundEffectRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.titleRes
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedIntervalData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedTeamData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboard.teamdisplay.TeamDisplay
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboard.uievent.IntervalEditor
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
class ScoreboardViewModel @Inject constructor(
    private val resources: Resources,
    private val getSportUseCase: GetSportUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val insertHistoryUseCase: InsertHistoryUseCase,
    private val scoreboardManager: ScoreboardManager,
    private val timeConversionUseCase: TimeConversionUseCase,
    savedStateHandle: SavedStateHandle,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {

    /**
     * Primary Display score
     */
    private val _primaryDisplayedScoreInfo = MutableStateFlow(com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(listOf(), com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank))
    val primaryDisplayedScoreInfo: StateFlow<com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo> = _primaryDisplayedScoreInfo.asStateFlow()
    private val primaryScoresUpdateListener: (com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo) -> Unit = {
        _primaryDisplayedScoreInfo.value = it
    }

    /**
     * Secondary Display score
     */
    private val _secondaryDisplayedScoreInfo = MutableStateFlow(com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo(listOf(), com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore.Blank))
    val secondaryDisplayedScoreInfo: StateFlow<com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo> = _secondaryDisplayedScoreInfo.asStateFlow()
    private val secondaryScoresUpdateListener: (com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo) -> Unit = {
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
        _timeData.value = timeConversionUseCase.toTimeData(it)
        if (it <= 0L) {
            stopTimerJob()
        }
    }

    private var sportType: SportType? = null

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
            sendUiEvent(PlaySound(it))
        }
    }

    private val _simpleMode = MutableStateFlow(true)
    val simpleMode: StateFlow<Boolean> = _simpleMode.asStateFlow()

    private val _timerInProgress = MutableStateFlow(false)
    val timerInProgress: StateFlow<Boolean> = _timerInProgress.asStateFlow()

    private var timerJob: Job? = null

    private var sportIdentifier: com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier? = null

    private var timelineViewerTitle = ""
    private var timelineViewerIcon = SportIcon.BASKETBALL

    private var historyEntityId: Int? = null
    private var isHistoryTemporary = true

    private val _intervalLabel = MutableStateFlow<Label>(Label.Custom(""))
    val intervalLabel = _intervalLabel.asStateFlow()

    private val _secondaryScoreLabel = MutableStateFlow<Label>(Label.Custom(""))
    val secondaryScoreLabel = _secondaryScoreLabel.asStateFlow()

    private var secondaryScoreLabelList = listOf<Label>()

    init {
        savedStateHandle.get<com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier>(SPORT_IDENTIFIER)?.let { sId ->
            when (sId) {
                is com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier.Custom -> initWithId(sId.id)
                is com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier.Default -> initWithSportType(sId.sportType)
            }
            sportIdentifier = sId
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
            getSportUseCase(id)?.let {
                timelineViewerTitle = it.title
                timelineViewerIcon = it.icon
            }
        }
    }

    private fun initWithSportType(sportType: SportType) {
        this.sportType = sportType
        _intervalLabel.value = Label.Resource(sportType.intervalLabelRes())
        timelineViewerTitle = resources.getString(sportType.titleRes())
        timelineViewerIcon = sportType.icon
        getSportUseCase(resources.openRawResource(sportType.rawRes()))?.let { sportModel ->
            scoreboardManager.apply {
                winRule = sportModel.winRule
                intervalList = sportModel.intervalList
            }
            secondaryScoreLabelList = sportModel.intervalList.map {
                Label.Resource(sportType.secondaryScoreLabelRes())
            }
        }

    }

    fun onScoreChange(
        isPrimary: Boolean,
        scoreIndex: Int,
        incrementIndex: Int,
        main: Boolean,
    ) = scoreboardManager.updateScore(isPrimary, scoreIndex, incrementIndex, main)

    fun toTeamPicker(index: Int) = sendUiEvent(TeamPicker(index))

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
        sportIdentifier?.let { sId ->
            sendUiEvent(IntervalEditor(timeConversionUseCase.fromTimeData(timeData.value), currentInterval.value - 1, sId))
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
            sendUiEvent(TimelineViewer(it, currentInterval.value - 1))
        }
    }

    private suspend fun insertHistory(): Int {
        val intervalLabel = when (val l = intervalLabel.value) {
            is Label.Custom -> IntervalLabel.Custom(l.value)
            else -> sportType?.let {
                IntervalLabel.DefaultSport(it)
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
            com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel(
                id = historyEntityId,
                title = timelineViewerTitle,
                description = "",
                icon = timelineViewerIcon,
                lastModified = DateTime.now(),
                historicalScoreboard = historicalScoreboard,
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