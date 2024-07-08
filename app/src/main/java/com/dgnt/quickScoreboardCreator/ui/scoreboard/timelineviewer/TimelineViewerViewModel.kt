package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimelineViewerViewModel @Inject constructor(
    private val resources: Resources,
    private val getScoreboardUseCase: GetScoreboardUseCase,
    private val scoreboardLoader: ScoreboardLoader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private var intervalList = listOf<IntervalConfig>()

    private var intervalIndex = 1

    private val _labelInfo = MutableStateFlow(Pair<String?, Int?>(null, null))
    val labelInfo = _labelInfo.asStateFlow()

    private val _historicalScoreboard = MutableStateFlow<HistoricalScoreboard?>(null)
    val historicalScoreboard = _historicalScoreboard.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(Arguments.ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: savedStateHandle.get<ScoreboardType>(Arguments.TYPE)?.let {
            initWithScoreboardType(it)
        }
        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            intervalIndex = it + 1
        }
        savedStateHandle.get<HistoricalScoreboard>(Arguments.HISTORICAL_SCOREBOARD)?.let {
            _historicalScoreboard.value = it
        }
    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getScoreboardUseCase(id)?.let {

            }
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {
        _labelInfo.value = null to scoreboardType.intervalLabelRes
        scoreboardType.rawRes?.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let {
            intervalList = it.intervalList
        }
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onSave(){
        sendUiEvent(UiEvent.Done)
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}