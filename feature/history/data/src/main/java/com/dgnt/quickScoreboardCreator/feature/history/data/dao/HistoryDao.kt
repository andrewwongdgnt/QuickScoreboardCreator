package com.dgnt.quickScoreboardCreator.feature.history.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao : BaseDao<HistoryEntity> {

    @Query("SELECT * FROM historyentity")
    override fun getAll(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM historyentity WHERE id = :id")
    override suspend fun getById(id: Int): HistoryEntity?
}