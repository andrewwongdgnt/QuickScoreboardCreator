package com.dgnt.quickScoreboardCreator.feature.sport.domain.model

data class SportListItem(
    val sportIdentifier: SportIdentifier,
    val title: String,
    val description: String,
    val icon: SportIcon
)