package com.dgnt.quickScoreboardCreator.ui.scoreboardlist

import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.model.scoreboard.Scoreboard


sealed class ScoreboardListEvent {
    data object OnAdd: ScoreboardListEvent()
    data class OnEdit(val scoreboard: ScoreboardEntity): ScoreboardListEvent()
    data class OnDelete(val scoreboardList: List<ScoreboardEntity>): ScoreboardListEvent()
    data object OnUndoDelete: ScoreboardListEvent()
    data class OnLaunch(val scoreboard: ScoreboardEntity): ScoreboardListEvent()
}
