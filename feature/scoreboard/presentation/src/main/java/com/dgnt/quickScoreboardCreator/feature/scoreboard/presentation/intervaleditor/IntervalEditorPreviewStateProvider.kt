package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.intervaleditor

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label

class IntervalEditorPreviewStateProvider : CollectionPreviewParameterProvider<IntervalEditorState>(
    listOf(
        IntervalEditorState(
            minuteString = "12",
            secondString = "8",
            intervalString = "1",
            label = Label.Resource(R.string.quarter),
            errors = emptySet()
        ),
        IntervalEditorState(
            minuteString = "12",
            secondString = "8",
            intervalString = "1",
            label = Label.Resource(R.string.quarter),
            errors = setOf(IntervalEditorErrorType.Time.Invalid(12, 0))
        ),
        IntervalEditorState(
            minuteString = "",
            secondString = "8",
            intervalString = "1",
            label = Label.Resource(R.string.quarter),
            errors = setOf(IntervalEditorErrorType.Time.Empty)
        ),
        IntervalEditorState(
            minuteString = "0",
            secondString = "0",
            intervalString = "1",
            label = Label.Resource(R.string.quarter),
            errors = setOf(IntervalEditorErrorType.Time.Zero)
        ),
        IntervalEditorState(
            minuteString = "10",
            secondString = "0",
            intervalString = "",
            label = Label.Resource(R.string.quarter),
            errors = setOf(IntervalEditorErrorType.Interval.Empty)
        ),
        IntervalEditorState(
            minuteString = "12",
            secondString = "8",
            intervalString = "1",
            label = Label.Resource(R.string.quarter),
            errors = setOf(IntervalEditorErrorType.Interval.Invalid(22))
        )

    )
)