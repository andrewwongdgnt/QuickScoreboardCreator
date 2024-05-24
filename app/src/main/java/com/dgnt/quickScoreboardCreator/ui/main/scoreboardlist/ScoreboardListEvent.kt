package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType


sealed class ScoreboardListEvent {
    data object OnAdd : ScoreboardListEvent()
    data class OnEdit(val id: Int, val type: ScoreboardType) : ScoreboardListEvent()
    data class OnDelete(val id: Int) : ScoreboardListEvent()
    data object OnUndoDelete : ScoreboardListEvent()
    data class OnLaunch(val id: Int, val type: ScoreboardType) : ScoreboardListEvent()
    data object OnClearDeletedScoreboardList : ScoreboardListEvent()
}
