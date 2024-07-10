package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.common.Label
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalEndSoundType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.SCOREBOARD_IDENTIFIER
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedIntervalData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedTeamData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay.TeamDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardInteractionViewModel @Inject constructor(
    private val resources: Resources,
    private val getScoreboardUseCase: GetScoreboardUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val scoreboardLoader: ScoreboardLoader,
    private val scoreboardManager: ScoreboardManager,
    private val timeTransformer: TimeTransformer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

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
            teamList.value.getOrNull(index) ?: TeamDisplay.UnSelectedTeamDisplay
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
            timerJob?.cancel()
            _timerInProgress.value = false
        }
    }

    /**
     * Winners by team index
     */
    private val winnersUpdateListener: (Set<Int>) -> Unit = {
        //TODO handle winners
    }

    private val intervalOnEndListener: (IntervalEndSoundType) -> Unit = { soundType ->
        soundType.rawRes?.let {
            sendUiEvent(UiEvent.PlaySound(it))
        }
    }

    private val _simpleMode = MutableStateFlow(true)
    val simpleMode: StateFlow<Boolean> = _simpleMode.asStateFlow()

    private val _timerInProgress = MutableStateFlow(false)
    val timerInProgress: StateFlow<Boolean> = _timerInProgress.asStateFlow()

    private var timerJob: Job? = null

    private var scoreboardIdentifier: ScoreboardIdentifier? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _intervalLabel = MutableStateFlow<Label>(Label.CustomLabel(""))
    val intervalLabel = _intervalLabel.asStateFlow()

    private val _secondaryScoreLabel = MutableStateFlow<Label>(Label.CustomLabel(""))
    val secondaryScoreLabel = _secondaryScoreLabel.asStateFlow()

    private var secondaryScoreLabelList = listOf<Label>()

    init {
        savedStateHandle.get<ScoreboardIdentifier>(SCOREBOARD_IDENTIFIER)?.let { sId ->
            when (sId) {
                is ScoreboardIdentifier.CustomScoreboard -> initWithId(sId.id)
                is ScoreboardIdentifier.DefaultScoreboard -> initWithScoreboardType(sId.scoreboardType)
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

            }
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {
        _intervalLabel.value = Label.ResourceLabel(scoreboardType.intervalLabelRes)
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
                Label.ResourceLabel(scoreboardType.secondaryScoreLabelRes)
            }
        }

    }

    fun onScoreChange(
        isPrimary: Boolean,
        scoreIndex: Int,
        incrementIndex: Int,
        positive: Boolean,
    ) = scoreboardManager.updateScore(isPrimary, scoreIndex, incrementIndex, positive)

    fun toTeamPicker(index: Int) = sendUiEvent(UiEvent.TeamPicker(index))

    fun onTeamPick(updatedTeamData: UpdatedTeamData) {
        viewModelScope.launch {
            _teamList.value = (0 until scoreboardManager.currentTeamSize).mapNotNull { index ->
                if (updatedTeamData.scoreIndex == index) {
                    getTeamUseCase(updatedTeamData.teamId)?.let {
                        TeamDisplay.SelectedTeamDisplay(it.title, it.icon)
                    }
                } else {
                    teamList.value.getOrNull(index)
                }
            }
        }
    }

    fun onTimerPause(reset: Boolean) {
        timerJob?.cancel()
        _timerInProgress.value = false
        if (reset) {
            scoreboardManager.resetTime()
        }
    }

    fun onTimerStart() {
        timerJob?.cancel()
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
        timerJob?.cancel()
        _timerInProgress.value = false
        scoreboardIdentifier?.let { sId ->
            sendUiEvent(UiEvent.IntervalEditor(timeTransformer.fromTimeData(timeData.value), currentInterval.value - 1, sId))
        }
    }

    fun onIntervalEdit(updatedIntervalData: UpdatedIntervalData) {
        updatedIntervalData.takeUnless { it.timeValue < 0 || it.intervalIndex < 0 }?.let {
            timerJob?.cancel()
            _timerInProgress.value = false
            scoreboardManager.updateInterval(it.intervalIndex)
            scoreboardManager.updateTime(it.timeValue)

        }
    }

    fun toTimelineViewer() {
        sendUiEvent(UiEvent.TimelineViewer(scoreboardManager.createTimeline(), currentInterval.value - 1))
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}