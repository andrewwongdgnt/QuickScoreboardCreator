package com.dgnt.quickScoreboardCreator.data.team.dao

import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickScoreboardCreator.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao : BaseDao<TeamEntity> {

    @Query("SELECT * FROM teamentity")
    override fun getAll(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teamentity WHERE id = :id")
    override suspend fun getById(id: Int): TeamEntity?
}