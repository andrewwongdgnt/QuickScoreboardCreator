package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import com.dgnt.quickScoreboardCreator.domain.team.model.TeamItemData


sealed class TeamListEvent {
    data object OnAdd: TeamListEvent()
    data class OnEdit(val team: TeamItemData): TeamListEvent()
    data class OnDelete(val teamList: List<TeamItemData>): TeamListEvent()
    data object OnUndoDelete: TeamListEvent()
}
