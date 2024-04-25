package com.dgnt.quickScoreboardCreator.data.scoreboard.dao

import androidx.room.Dao

import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import com.dgnt.quickScoreboardCreator.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity

@Dao
interface ScoreboardDao : BaseDao<ScoreboardEntity> {

    @Query("SELECT * FROM scoreboardentity")
    override fun getAll(): Flow<List<ScoreboardEntity>>

    @Query("SELECT * FROM scoreboardentity WHERE id = :id")
    override suspend fun getById(id: Int): ScoreboardEntity?
}