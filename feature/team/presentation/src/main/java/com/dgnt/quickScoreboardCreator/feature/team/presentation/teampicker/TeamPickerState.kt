package com.dgnt.quickScoreboardCreator.feature.team.presentation.teampicker

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.CategorizedTeamItemData

data class TeamPickerState(
    val categorizedTeamList: List<CategorizedTeamItemData> = emptyList()
)
