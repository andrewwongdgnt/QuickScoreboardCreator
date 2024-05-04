package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon

data class TeamItemData(
    val id: Int?,
    val title: String,
    val description: String,
    val teamIcon: TeamIcon
)