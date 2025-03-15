package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.intervaleditor

import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label

data class IntervalEditorState(
    val minuteString: String = "",
    val secondString: String = "",
    val intervalString: String = "",
    val label: Label = Label.Custom(""),
    val errors: Set<IntervalEditorErrorType> = emptySet()
)
