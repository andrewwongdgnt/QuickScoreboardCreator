package com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent

import kotlinx.coroutines.flow.Flow

interface UiEventHandler {
    val uiEvent: Flow<UiEvent>

    fun sendUiEvent(event: UiEvent)
}