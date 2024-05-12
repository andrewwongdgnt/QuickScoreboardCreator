package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntervalEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentTimeValue = 0L
    private var initialTimeValue = 0L
    private var hasMaxTime = false

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Long>(Arguments.TIME_VALUE)?.let {
            currentTimeValue = it
        }
        savedStateHandle.get<Long>(Arguments.INITIAL_TIME_VALUE)?.let {
            initialTimeValue = it
        }
        savedStateHandle.get<Boolean>(Arguments.HAS_MAX_TIME)?.let {
            hasMaxTime = it
        }
    }

    fun onEvent(event: IntervalEditorEvent) {

        when (event) {
            is IntervalEditorEvent.OnDismiss ->
                sendUiEvent(UiEvent.Done)
        }

    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}