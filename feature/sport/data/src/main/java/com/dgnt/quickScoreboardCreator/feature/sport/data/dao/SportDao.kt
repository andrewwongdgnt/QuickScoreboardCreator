package com.dgnt.quickScoreboardCreator.feature.sport.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.feature.sport.data.entity.SportEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface SportDao : BaseDao<SportEntity> {

    @Query("SELECT * FROM sportentity")
    override fun getAll(): Flow<List<SportEntity>>

    @Query("SELECT * FROM sportentity WHERE id = :id")
    override suspend fun getById(id: Int): SportEntity?
}