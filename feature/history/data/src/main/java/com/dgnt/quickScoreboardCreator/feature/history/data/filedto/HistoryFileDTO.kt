package com.dgnt.quickScoreboardCreator.feature.history.data.filedto

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import org.joda.time.DateTime

data class HistoryFileDTO (
    val title: String,
    val description: String,
    val icon: SportIcon,
    val lastModified: DateTime,
    val createdAt: DateTime,
)