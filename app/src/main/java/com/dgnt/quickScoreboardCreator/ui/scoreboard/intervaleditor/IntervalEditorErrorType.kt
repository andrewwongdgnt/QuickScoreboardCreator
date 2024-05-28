package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

sealed class IntervalEditorErrorType {
    sealed class TimeErrorType : IntervalEditorErrorType() {
        data class Time(val min: Int, val second: Int) : TimeErrorType()
        data object EmptyTime : TimeErrorType()
        data object ZeroTime : TimeErrorType()
    }

    sealed class IntervalErrorType : IntervalEditorErrorType() {
        data class Interval(val value: Int) : IntervalErrorType()
        data object EmptyInterval : IntervalErrorType()
    }

}