package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
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

    var incrementList by mutableStateOf(emptyList<List<Int>>())
        private set
    var displayedScoreInfo by mutableStateOf(DisplayedScoreInfo(listOf(), DisplayedScore.Blank))
        private set

    var labelInfo by mutableStateOf(Pair<String?, Int?>(null, null))
        private set

    var currentInterval by mutableIntStateOf(1)
        private set

    var teamList by mutableStateOf(emptyList<TeamDisplay>())

    private var timeValue = 0L
        set(value) {
            val newValue = 0L.coerceAtLeast(value)
            timeData = timeTransformer.toTimeData(newValue)
            scoreboardManager.setTime(newValue)
            field = newValue
        }

    var timeData by mutableStateOf(TimeData(0, 0, 0))
        private set
    var timerInProgress by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    private var id: Int = -1
    private var scoreboardType: ScoreboardType = ScoreboardType.NONE

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
            this.id = id
        } ?: savedStateHandle.get<ScoreboardType>(TYPE)?.let {
            initWithScoreboardType(it)
            this.scoreboardType = it
        }

        teamList = (0 until scoreboardManager.incrementList.size).map { TeamDisplay.UnSelectedTeamDisplay }
        viewModelScope.launch {
            snapshotFlow { currentInterval }
                .collect {
                    scoreboardManager.currentIntervalIndex = it - 1
                }
        }
    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getScoreboardUseCase(id)?.let {

            }
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {
        labelInfo = null to scoreboardType.intervalLabelRes
        scoreboardType.rawRes?.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let {
            currentInterval = 1
            scoreboardManager.apply {
                scoreCarriesOver = it.scoreCarriesOver
                intervalList = it.intervalList.map {
                    it.scoreInfo.toScoreInfo() to it.intervalData.toIntervalData()
                }
            }
            incrementList = scoreboardManager.incrementList
            displayedScoreInfo = scoreboardManager.getScores()
            timeValue = scoreboardManager.getInitialTime()

        }

    }

    fun onEvent(event: ScoreboardInteractionEvent) {
        when (event) {
            is ScoreboardInteractionEvent.UpdateScore -> {
                scoreboardManager.updateScore(event.scoreIndex, event.incrementIndex, event.positive)
                displayedScoreInfo = scoreboardManager.getScores()
            }

            is ScoreboardInteractionEvent.UpdateTeam -> {
                sendUiEvent(UiEvent.TeamPicker(event.scoreIndex))
            }

            is ScoreboardInteractionEvent.PauseTimer -> {
                timerJob?.cancel()
                timerInProgress = false
                if (event.reset) {
                    timeValue = scoreboardManager.getInitialTime()
                }

            }

            ScoreboardInteractionEvent.StartTimer -> {
                timerJob?.cancel()
                if (timeValue <= 0L && !scoreboardManager.isTimeIncreasing()) {
                    timerInProgress = false
                    return
                }

                timerJob = viewModelScope.launch {
                    while (true) {
                        delay(100)
                        if (scoreboardManager.isTimeIncreasing())
                            timeValue += 100L
                        else
                            timeValue -= 100L

                        if (timeValue == 0L) {
                            timerJob?.cancel()
                            timerInProgress = false
                            break
                        }
                    }
                }
                timerInProgress = true

            }

            is ScoreboardInteractionEvent.UpdatedTeam -> {
                viewModelScope.launch {
                    teamList = (0 until scoreboardManager.incrementList.size).mapNotNull { index ->
                        if (event.updatedTeamData.scoreIndex == index) {
                            getTeamUseCase(event.updatedTeamData.teamId)?.let {
                                TeamDisplay.SelectedTeamDisplay(it.title, it.teamIcon)
                            }
                        } else {
                            teamList.getOrNull(index)
                        }
                    }
                }
            }

            ScoreboardInteractionEvent.UpdateInterval -> {
                timerJob?.cancel()
                timerInProgress = false
                sendUiEvent(UiEvent.IntervalEditor(timeValue, scoreboardManager.currentIntervalIndex, id, scoreboardType))
            }

            is ScoreboardInteractionEvent.UpdatedInterval -> {
                event.takeUnless { it.updatedIntervalData.timeValue < 0 || it.updatedIntervalData.intervalIndex < 0 }?.let {
                    timerJob?.cancel()
                    timerInProgress = false
                    timeValue = it.updatedIntervalData.timeValue
                    currentInterval = it.updatedIntervalData.intervalIndex + 1
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