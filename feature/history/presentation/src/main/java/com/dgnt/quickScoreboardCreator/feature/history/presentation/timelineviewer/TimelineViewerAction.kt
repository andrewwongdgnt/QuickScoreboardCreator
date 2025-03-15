package com.dgnt.quickScoreboardCreator.feature.history.presentation.timelineviewer

sealed interface TimelineViewerAction {
    data object Dismiss : TimelineViewerAction
    data class NewInterval(val next: Boolean) : TimelineViewerAction
}