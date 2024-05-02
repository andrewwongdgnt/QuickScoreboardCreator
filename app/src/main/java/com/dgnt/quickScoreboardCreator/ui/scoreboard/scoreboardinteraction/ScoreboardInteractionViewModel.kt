package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.loader.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager.ScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.TYPE
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
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
    private val scoreboardLoader: ScoreboardLoader,
    private val scoreboardManager: ScoreboardManager,
    private val timeTransformer: TimeTransformer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var incrementList by mutableStateOf(emptyList<List<Int>>())
        private set
    var displayedScoreInfo by mutableStateOf(DisplayedScoreInfo(listOf(), DisplayedScore.Blank))
        private set

    private var timeValue = 0L
        set(value) {
            val newValue = 0L.coerceAtLeast(value)
            timeData = timeTransformer(newValue)
            field = newValue
        }

    var timeData by mutableStateOf(TimeData(0, 0, 0))
        private set
    var timerInProgress by mutableStateOf(false)
        private set

    private var timerJob: Job? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: savedStateHandle.get<ScoreboardType>(TYPE)?.let {
            initWithScoreboardType(it)
        }

    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getScoreboardUseCase(id)?.let {

            }
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {

        scoreboardType.rawRes?.let { rawRes ->
            resources.openRawResource(rawRes).let { ins ->
                scoreboardLoader.load(ins)
            } as DefaultScoreboardConfig?
        }?.let {
            scoreboardManager.apply {
                currentIntervalIndex = 0
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

            is ScoreboardInteractionEvent.PauseTimer -> {
                timerJob?.cancel()
                timerInProgress = false
                if (event.reset) {
                    timeValue = scoreboardManager.getInitialTime()
                    scoreboardManager.setTime(timeValue)
                }

            }

            is ScoreboardInteractionEvent.SkipTime -> {
                //10 seconds skip
                val skipValue = 10000L
                if ((event.value && scoreboardManager.isTimeIncreasing()) || (!event.value && !scoreboardManager.isTimeIncreasing()))
                    timeValue += skipValue
                else
                    timeValue -= skipValue
            }

            is ScoreboardInteractionEvent.StartTimer -> {
                timerJob?.cancel()
                if (timeValue <= 0L) {
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

                        scoreboardManager.setTime(timeValue)
                        if (timeValue == 0L) {
                            timerJob?.cancel()
                            timerInProgress = false
                            break
                        }
                    }
                }
                timerInProgress = true

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