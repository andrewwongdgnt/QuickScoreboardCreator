package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.TYPE
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
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
        secondaryScoreLabelInfoList.getOrNull(it)?.let { secondaryScoreLabelInfo ->
            _secondaryScoreLabelInfo.value = secondaryScoreLabelInfo
        }
        onEvent(ScoreboardInteractionEvent.PauseTimer(true))
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

    private val _timerInProgress = MutableStateFlow(false)
    val timerInProgress: StateFlow<Boolean> = _timerInProgress.asStateFlow()

    private var timerJob: Job? = null

    private var id: Int = -1
    private var scoreboardType: ScoreboardType = ScoreboardType.NONE

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _intervalLabelInfo = MutableStateFlow(Pair<String?, Int?>(null, null))
    val intervalLabelInfo: StateFlow<Pair<String?, Int?>> = _intervalLabelInfo.asStateFlow()

    private val _secondaryScoreLabelInfo = MutableStateFlow(Pair<String?, Int?>(null, null))
    val secondaryScoreLabelInfo: StateFlow<Pair<String?, Int?>> = _secondaryScoreLabelInfo.asStateFlow()

    private var secondaryScoreLabelInfoList = listOf<Pair<String?, Int?>>()

    init {
        savedStateHandle.get<Int>(ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
            this.id = id
        } ?: savedStateHandle.get<ScoreboardType>(TYPE)?.let {
            initWithScoreboardType(it)
            this.scoreboardType = it
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
        _intervalLabelInfo.value = null to scoreboardType.intervalLabelRes
        scoreboardType.rawRes?.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let { defaultScoreboardConfig ->
            scoreboardManager.apply {
                winRule = defaultScoreboardConfig.winRuleType.toWinRule()
                intervalList = defaultScoreboardConfig.intervalList.map {
                    it.scoreInfo.toScoreInfo() to it.intervalData.toIntervalData()
                }
            }
            secondaryScoreLabelInfoList = defaultScoreboardConfig.intervalList.map {
                it.scoreInfo.secondaryScoreLabel to scoreboardType.secondaryScoreLabelRes
            }
        }

    }

    fun onEvent(event: ScoreboardInteractionEvent) {
        when (event) {
            is ScoreboardInteractionEvent.UpdateScore -> {
                scoreboardManager.updateScore(event.isPrimary, event.scoreIndex, event.incrementIndex, event.positive)
            }

            is ScoreboardInteractionEvent.UpdateTeam -> {
                sendUiEvent(UiEvent.TeamPicker(event.scoreIndex))
            }

            is ScoreboardInteractionEvent.PauseTimer -> {
                timerJob?.cancel()
                _timerInProgress.value = false
                if (event.reset) {
                    scoreboardManager.resetTime()
                }

            }

            ScoreboardInteractionEvent.StartTimer -> {
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

            is ScoreboardInteractionEvent.UpdatedTeam -> {
                viewModelScope.launch {
                    _teamList.value = (0 until scoreboardManager.currentTeamSize).mapNotNull { index ->
                        if (event.updatedTeamData.scoreIndex == index) {
                            getTeamUseCase(event.updatedTeamData.teamId)?.let {
                                TeamDisplay.SelectedTeamDisplay(it.title, it.teamIcon)
                            }
                        } else {
                            teamList.value.getOrNull(index)
                        }
                    }
                }
            }

            ScoreboardInteractionEvent.UpdateInterval -> {
                timerJob?.cancel()
                _timerInProgress.value = false
                sendUiEvent(UiEvent.IntervalEditor(timeTransformer.fromTimeData(timeData.value), currentInterval.value - 1, id, scoreboardType))
            }

            is ScoreboardInteractionEvent.UpdatedInterval -> {
                event.takeUnless { it.updatedIntervalData.timeValue < 0 || it.updatedIntervalData.intervalIndex < 0 }?.let {
                    timerJob?.cancel()
                    _timerInProgress.value = false
                    scoreboardManager.updateInterval(it.updatedIntervalData.intervalIndex)
                    scoreboardManager.updateTime(it.updatedIntervalData.timeValue)

                }

            }
        }
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