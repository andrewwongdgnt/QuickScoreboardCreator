package com.dgnt.quickScoreboardCreator.domain.history.model

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import org.joda.time.DateTime

data class HistoryItemData(
    val id: Long,
    val title: String,
    val icon: ScoreboardIcon,
    val lastLookedAt: DateTime,
)