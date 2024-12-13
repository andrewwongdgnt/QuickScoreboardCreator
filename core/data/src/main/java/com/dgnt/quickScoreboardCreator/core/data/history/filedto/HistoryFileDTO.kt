package com.dgnt.quickScoreboardCreator.core.data.history.filedto

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIcon
import org.joda.time.DateTime

data class HistoryFileDTO (
    val title: String,
    val description: String,
    val icon: SportIcon,
    val lastModified: DateTime,
    val createdAt: DateTime,
)