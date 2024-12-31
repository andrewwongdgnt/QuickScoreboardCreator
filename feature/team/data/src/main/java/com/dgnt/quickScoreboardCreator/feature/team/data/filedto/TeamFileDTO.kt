package com.dgnt.quickScoreboardCreator.feature.team.data.filedto

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon


data class TeamFileDTO (
    val title: String,
    val description: String,
    val icon: TeamIcon
)