package com.dgnt.quickScoreboardCreator.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity

@Database(
    entities = [ScoreboardEntity::class],
    version = 1
)
abstract class QSCDatabase: RoomDatabase() {
}