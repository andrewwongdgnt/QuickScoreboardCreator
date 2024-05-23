package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon

sealed class TeamDetailsEvent {
    data object OnConfirm : TeamDetailsEvent()
    data object OnDismiss : TeamDetailsEvent()
    data object OnTeamIconEdit : TeamDetailsEvent()
    data class OnNewTeamIcon(val teamIcon: TeamIcon) : TeamDetailsEvent()
    data class OnTitleChange(val title: String) : TeamDetailsEvent()
    data class OnDescriptionChange(val descriptionChange: String) : TeamDetailsEvent()
}