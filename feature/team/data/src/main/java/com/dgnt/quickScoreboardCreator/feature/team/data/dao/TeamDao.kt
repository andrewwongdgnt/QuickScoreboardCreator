package com.dgnt.quickScoreboardCreator.feature.team.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao : BaseDao<TeamEntity> {

    @Query("SELECT * FROM teamentity")
    override fun getAll(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teamentity WHERE id = :id")
    override suspend fun getById(id: Int): TeamEntity?
}