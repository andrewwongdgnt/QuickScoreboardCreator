package com.dgnt.quickScoreboardCreator.core.data.sport.dao

import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.sport.entity.SportEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SportDao : BaseDao<SportEntity> {

    @Query("SELECT * FROM sportentity")
    override fun getAll(): Flow<List<SportEntity>>

    @Query("SELECT * FROM sportentity WHERE id = :id")
    override suspend fun getById(id: Int): SportEntity?
}