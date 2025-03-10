package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamdetails

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon

sealed interface TeamDetailsAction {
    data object Confirm : TeamDetailsAction
    data object Dismiss : TeamDetailsAction
    data object Delete : TeamDetailsAction
    data class TitleChange(val title: String) : TeamDetailsAction
    data class DescriptionChange(val description: String) : TeamDetailsAction
    data class IconEdit(val changing: Boolean) : TeamDetailsAction
    data class IconChange(val icon: TeamIcon) : TeamDetailsAction

}