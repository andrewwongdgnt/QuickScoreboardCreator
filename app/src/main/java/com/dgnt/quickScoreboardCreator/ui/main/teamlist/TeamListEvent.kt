package com.dgnt.quickScoreboardCreator.ui.main.teamlist


sealed class TeamListEvent {
    data object OnAdd: TeamListEvent()
    data class OnEdit(val id: Int): TeamListEvent()
    data class OnDelete(val id: Int): TeamListEvent()
    data object OnUndoDelete: TeamListEvent()
    data object OnClearDeletedTeamList: TeamListEvent()
}
