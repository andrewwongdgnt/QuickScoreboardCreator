package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

sealed class IntervalEditorEvent {
    data object OnDismiss : IntervalEditorEvent()
}