package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.intervaleditor

sealed interface IntervalEditorAction {
    data object Dismiss : IntervalEditorAction
    data object Confirm : IntervalEditorAction
    data class MinuteChange(val value: String) : IntervalEditorAction
    data class SecondChange(val value: String) : IntervalEditorAction
    data class IntervalChange(val value: String) : IntervalEditorAction

}