package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

sealed class IntervalEditorEvent {
    data object OnDismiss : IntervalEditorEvent()
    data object OnConfirm : IntervalEditorEvent()

    data class OnMinuteChange(val value: String): IntervalEditorEvent()
    data class OnSecondChange(val value: String): IntervalEditorEvent()
    data class OnIntervalChange(val value: String): IntervalEditorEvent()
}