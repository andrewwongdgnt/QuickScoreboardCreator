package com.dgnt.quickScoreboardCreator.core.data.scoreboard.dao

import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.entity.ScoreboardEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ScoreboardDao : BaseDao<ScoreboardEntity> {

    @Query("SELECT * FROM scoreboardentity")
    override fun getAll(): Flow<List<ScoreboardEntity>>

    @Query("SELECT * FROM scoreboardentity WHERE id = :id")
    override suspend fun getById(id: Int): ScoreboardEntity?
}