package com.dgnt.quickScoreboardCreator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dgnt.quickScoreboardCreator.database.converter.DateTimeConverter
import com.dgnt.quickScoreboardCreator.feature.history.data.converter.HistoricalScoreboardDataConverter
import com.dgnt.quickScoreboardCreator.feature.history.data.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.sport.data.converter.IntervalListConverter
import com.dgnt.quickScoreboardCreator.feature.sport.data.converter.WinRuleConverter
import com.dgnt.quickScoreboardCreator.feature.sport.data.dao.SportDao
import com.dgnt.quickScoreboardCreator.feature.sport.data.entity.SportEntity
import com.dgnt.quickScoreboardCreator.feature.team.data.dao.TeamDao
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity


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