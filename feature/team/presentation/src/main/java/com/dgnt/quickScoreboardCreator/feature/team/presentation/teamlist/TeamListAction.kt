package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamlist

sealed interface TeamListAction {
    data object Add : TeamListAction
    data class Edit(val id: Int) : TeamListAction
    data class Delete(val id: Int) : TeamListAction
    data object UndoDelete : TeamListAction
    data object ClearDeletedTeamList : TeamListAction
}