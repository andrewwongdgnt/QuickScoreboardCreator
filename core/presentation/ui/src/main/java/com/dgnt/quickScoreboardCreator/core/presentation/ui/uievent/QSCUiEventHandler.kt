package com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class QSCUiEventHandler : UiEventHandler, ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    override val uiEvent = _uiEvent.receiveAsFlow()
    override fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}