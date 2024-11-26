package com.dgnt.quickScoreboardCreator.core.domain.history.model

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import org.joda.time.DateTime

data class HistoryItemData(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ScoreboardIcon,
    val lastModified: DateTime,
    val createdAt: DateTime,
)