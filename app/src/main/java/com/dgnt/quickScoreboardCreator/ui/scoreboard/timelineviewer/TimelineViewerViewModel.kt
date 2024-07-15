package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private var intervalIndex = 1

    private var historicalScoreboard: HistoricalScoreboard? = null

    private var _historicalInterval = MutableStateFlow<HistoricalInterval?>(null)
    val historicalInterval = _historicalInterval.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {

        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            intervalIndex = it
        }
        savedStateHandle.get<HistoricalScoreboard>(Arguments.HISTORICAL_SCOREBOARD)?.let {
            historicalScoreboard = it
            _historicalInterval.value = historicalScoreboard?.historicalIntervalMap?.get(intervalIndex)

        }
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onSave() {
        sendUiEvent(UiEvent.Done)
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}