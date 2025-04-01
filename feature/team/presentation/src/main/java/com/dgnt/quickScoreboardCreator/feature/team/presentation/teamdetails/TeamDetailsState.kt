package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamdetails

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon

data class TeamDetailsState(
    val title: String = "",
    val description: String = "",
    val iconState: TeamIconState = TeamIconState.Initial,
    val isNewEntity: Boolean = true,
    val valid: Boolean = false
)

sealed interface TeamIconState {
    data object Initial : TeamIconState
    sealed class Picked(val teamIcon: TeamIcon) : TeamIconState {
        data class Changing(private val _teamIcon: TeamIcon) : Picked(_teamIcon)
        data class Displaying(private val _teamIcon: TeamIcon) : Picked(_teamIcon)
    }
}