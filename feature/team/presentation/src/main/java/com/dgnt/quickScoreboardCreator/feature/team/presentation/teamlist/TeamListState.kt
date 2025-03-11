package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamlist

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.CategorizedTeamItemData

data class TeamListState (
    val categorizedTeamList: List<CategorizedTeamItemData> = emptyList()
)