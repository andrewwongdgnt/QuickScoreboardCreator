package com.dgnt.quickScoreboardCreator.core.data.team.filedto

import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon

data class TeamFileDTO (
    val title: String,
    val description: String,
    val icon: TeamIcon
)