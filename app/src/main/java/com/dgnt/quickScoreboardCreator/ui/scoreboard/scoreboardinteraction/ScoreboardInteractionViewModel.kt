package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.loader.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager.ScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var incrementList by mutableStateOf(emptyList<List<Int>>())
    var displayedScoreInfo by mutableStateOf(DisplayedScoreInfo(listOf(), DisplayedScore.Blank))

    var timeValue by mutableLongStateOf(0L)

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
                if (event.reset)
                    scoreboardManager.setTime(0)

            }
            is ScoreboardInteractionEvent.SkipTime -> {


            }
            is ScoreboardInteractionEvent.StartTimer -> {
                timerJob?.cancel()
                timerJob = viewModelScope.launch {
                    while (true) {
                        delay(100)
                        if (scoreboardManager.isTimeIncreasing())
                            timeValue+=100
                        else
                            timeValue-=100
                    }
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