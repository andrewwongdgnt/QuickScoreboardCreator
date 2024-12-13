package com.dgnt.quickScoreboardCreator.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dgnt.quickScoreboardCreator.core.data.history.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.data.sport.dao.SportDao
import com.dgnt.quickScoreboardCreator.core.data.sport.entity.SportEntity
import com.dgnt.quickScoreboardCreator.core.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity


@Database(
    entities = [SportEntity::class, TeamEntity::class, HistoryEntity::class],
    version = 1
)
@TypeConverters(WinRuleConverter::class, DateTimeConverter::class, IntervalListConverter::class, HistoricalScoreboardDataConverter::class)
abstract class QSCDatabase : RoomDatabase() {
    abstract val sportDao: SportDao
    abstract val teamDao: TeamDao
    abstract val historyDao: HistoryDao
}