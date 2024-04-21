package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist


sealed class ScoreboardListEvent {
    data object OnAdd: ScoreboardListEvent()
    data class OnEdit(val scoreboard: ScoreboardItemData): ScoreboardListEvent()
    data class OnDelete(val scoreboardList: List<ScoreboardItemData>): ScoreboardListEvent()
    data object OnUndoDelete: ScoreboardListEvent()
    data class OnLaunch(val scoreboard: ScoreboardItemData): ScoreboardListEvent()
}
