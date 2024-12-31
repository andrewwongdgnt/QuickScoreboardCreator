package com.dgnt.quickScoreboardCreator.feature.history.domain.model

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import org.joda.time.DateTime

data class HistoryModel(
    val id: Int? = null,
    val title: String,
    val description: String,
    val icon: SportIcon,
    val lastModified: DateTime,
    val createdAt: DateTime = DateTime.now(),
    val historicalScoreboard: HistoricalScoreboard,
    val temporary: Boolean,
)
