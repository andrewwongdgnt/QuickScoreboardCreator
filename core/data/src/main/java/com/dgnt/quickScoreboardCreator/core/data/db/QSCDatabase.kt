package com.dgnt.quickScoreboardCreator.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dgnt.quickScoreboardCreator.core.data.history.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.dao.ScoreboardDao
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity


@Database(
    entities = [ScoreboardEntity::class, TeamEntity::class, HistoryEntity::class],
    version = 1
)
@TypeConverters(WinRuleConverter::class, DateTimeConverter::class, IntervalListConverter::class, HistoricalScoreboardDataConverter::class)
abstract class QSCDatabase : RoomDatabase() {
    abstract val scoreboardDao: ScoreboardDao
    abstract val teamDao: TeamDao
    abstract val historyDao: HistoryDao
}