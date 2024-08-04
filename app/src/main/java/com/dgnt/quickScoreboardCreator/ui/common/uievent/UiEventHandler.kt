package com.dgnt.quickScoreboardCreator.ui.common.uievent

import kotlinx.coroutines.flow.Flow

interface UiEventHandler {
    val uiEvent: Flow<UiEvent>

    fun sendUiEvent(event: UiEvent)
}