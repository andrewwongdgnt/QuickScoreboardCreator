package com.dgnt.quickScoreboardCreator.data.base.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dgnt.quickScoreboardCreator.data.scoreboard.dao.ScoreboardDao
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity

@Database(
    entities = [ScoreboardEntity::class, TeamEntity::class],
    version = 1
)
abstract class QSCDatabase : RoomDatabase() {
    abstract val scoreboardDao: ScoreboardDao
    abstract val teamDao: TeamDao
}