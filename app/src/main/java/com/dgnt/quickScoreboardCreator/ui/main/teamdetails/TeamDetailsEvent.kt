package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon

sealed class TeamDetailsEvent {
    data object OnConfirm : TeamDetailsEvent()
    data object OnDismiss : TeamDetailsEvent()
    data object OnIconEdit : TeamDetailsEvent()
    data class OnNewIcon(val icon: TeamIcon) : TeamDetailsEvent()
    data class OnTitleChange(val title: String) : TeamDetailsEvent()
    data class OnDescriptionChange(val descriptionChange: String) : TeamDetailsEvent()
}