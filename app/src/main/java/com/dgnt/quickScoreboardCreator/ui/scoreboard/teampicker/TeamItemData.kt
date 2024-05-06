package com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker

import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon

data class TeamItemData(
    val id: Int,
    val title: String,
    val teamIcon: TeamIcon
)