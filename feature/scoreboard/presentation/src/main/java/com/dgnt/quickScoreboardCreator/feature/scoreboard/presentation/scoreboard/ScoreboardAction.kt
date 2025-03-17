package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard

sealed interface ScoreboardAction {
    data class ScoreChange(val isPrimary: Boolean, val scoreIndex: Int, val incrementIndex: Int, val main: Boolean) : ScoreboardAction
    data class ToTeamPicker(val index: Int) : ScoreboardAction
    data class TeamPick(val updatedTeamData: UpdatedTeamData) : ScoreboardAction
    data class TimerPause(val reset:Boolean) : ScoreboardAction
    data object TimerStart : ScoreboardAction
    data class ToggleModeChange(val isSimpleMode: Boolean) : ScoreboardAction
    data object ToIntervalEditor : ScoreboardAction
    data class IntervalEdit(val updatedIntervalData: UpdatedIntervalData) : ScoreboardAction
    data object ToTimelineViewer : ScoreboardAction
}