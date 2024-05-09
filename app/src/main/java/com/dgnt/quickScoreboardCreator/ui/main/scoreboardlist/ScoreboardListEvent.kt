package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardItemData


sealed class ScoreboardListEvent {
    data object OnAdd: ScoreboardListEvent()
    data class OnEdit(val scoreboard: ScoreboardItemData): ScoreboardListEvent()
    data class OnDelete(val id: Int): ScoreboardListEvent()
    data object OnUndoDelete: ScoreboardListEvent()
    data class OnLaunch(val scoreboard: ScoreboardItemData): ScoreboardListEvent()
    data object OnClearDeletedScoreboardList: ScoreboardListEvent()
}
