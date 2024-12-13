package com.dgnt.quickScoreboardCreator.core.domain.sport.model

data class SportListItem(
    val sportIdentifier: SportIdentifier,
    val title: String,
    val description: String,
    val icon: SportIcon
)