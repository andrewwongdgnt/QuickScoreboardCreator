package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

sealed interface IntervalEditorErrorType {
    sealed interface Time : IntervalEditorErrorType {
        data class Invalid(val min: Int, val second: Int) : Time
        data object Empty : Time
        data object Zero : Time
    }

    sealed interface Interval : IntervalEditorErrorType {
        data class Invalid(val value: Int) : Interval
        data object Empty : Interval
    }

}