package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportListItem
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType

data class SportListState(
    val defaultSportList: List<SportType> = emptyList(),
    val customSportList: List<SportListItem> = emptyList()
)