package com.dgnt.quickScoreboardCreator.core.data.history.config

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import org.joda.time.DateTime

data class HistoryConfig (
    val title: String,
    val description: String,
    val icon: ScoreboardIcon,
    val lastModified: DateTime,
    val createdAt: DateTime,
)