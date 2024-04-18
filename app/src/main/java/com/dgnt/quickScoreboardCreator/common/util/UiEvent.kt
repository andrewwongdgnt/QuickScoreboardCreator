package com.dgnt.quickScoreboardCreator.common.util

sealed class UiEvent {
    data object PopBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
}