package com.dgnt.quickScoreboardCreator.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dgnt.quickScoreboardCreator.data.history.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.data.scoreboard.dao.ScoreboardDao
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity

@Database(
    entities = [ScoreboardEntity::class, TeamEntity::class, HistoryEntity::class],
    version = 1
)
@TypeConverters(DateTimeConverter::class, IntervalListConverters::class, HistoricalScoreboardDataConverter::class)
abstract class QSCDatabase : RoomDatabase() {
    abstract val scoreboardDao: ScoreboardDao
    abstract val teamDao: TeamDao
    abstract val historyDao: HistoryDao
}