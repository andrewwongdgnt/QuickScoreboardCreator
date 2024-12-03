package com.dgnt.quickScoreboardCreator.core.data.history.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import org.joda.time.DateTime

@Entity
data class HistoryEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val icon: ScoreboardIcon,
    val lastModified: DateTime,
    val createdAt: DateTime = DateTime.now(),
    val historicalScoreboard: HistoricalScoreboardData,
    val temporary: Boolean,
)


