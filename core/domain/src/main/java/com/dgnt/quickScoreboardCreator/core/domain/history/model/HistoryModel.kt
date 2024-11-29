package com.dgnt.quickScoreboardCreator.core.domain.history.model

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import org.joda.time.DateTime

data class HistoryModel(
    val id: Int? = null,
    val title: String,
    val description: String,
    val icon: ScoreboardIcon,
    val lastModified: DateTime,
    val createdAt: DateTime = DateTime.now(),
    val historicalScoreboard: HistoricalScoreboard,
    val temporary: Boolean,
)
