package com.dgnt.quickScoreboardCreator.feature.history.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import org.joda.time.DateTime

@Entity
data class HistoryEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val icon: SportIcon,
    val lastModified: DateTime,
    val createdAt: DateTime = DateTime.now(),
    val historicalScoreboard: HistoricalScoreboardData,
    val temporary: Boolean,
)


