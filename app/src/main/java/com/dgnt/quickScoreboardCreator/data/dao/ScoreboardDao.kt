package com.dgnt.quickScoreboardCreator.data.dao

import androidx.room.Dao

import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity

@Dao
interface ScoreboardDao : BaseDao<ScoreboardEntity> {

    @Query("SELECT * FROM scoreboardentity")
    override fun getAll(): Flow<List<ScoreboardEntity>>
}