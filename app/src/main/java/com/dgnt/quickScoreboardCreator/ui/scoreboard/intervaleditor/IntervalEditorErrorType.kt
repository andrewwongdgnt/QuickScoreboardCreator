package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

sealed class IntervalEditorErrorType {
    data class Time(val min: Int, val second: Int) : IntervalEditorErrorType()
    data class Interval(val value: Int) : IntervalEditorErrorType()
}