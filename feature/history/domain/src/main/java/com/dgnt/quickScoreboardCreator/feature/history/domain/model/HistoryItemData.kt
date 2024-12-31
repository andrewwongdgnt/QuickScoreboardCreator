package com.dgnt.quickScoreboardCreator.feature.history.domain.model

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import org.joda.time.DateTime

data class HistoryItemData(
    val id: Int,
    val title: String,
    val description: String,
    val icon: SportIcon,
    val lastModified: DateTime,
    val createdAt: DateTime,
)