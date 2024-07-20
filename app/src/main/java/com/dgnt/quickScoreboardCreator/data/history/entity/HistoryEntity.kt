package com.dgnt.quickScoreboardCreator.data.history.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import org.joda.time.DateTime

@Entity
data class HistoryEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val icon: ScoreboardIcon,
    val lastLookedAt: DateTime,
    val historicalScoreboard: HistoricalScoreboard
)