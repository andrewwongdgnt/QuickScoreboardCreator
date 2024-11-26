package com.dgnt.quickScoreboardCreator.data.base.repository

import androidx.room.Transaction
import com.dgnt.quickScoreboardCreator.core.domain.base.repository.Repository
import com.dgnt.quickScoreboardCreator.data.base.dao.BaseDao

abstract class BaseRepository<T>(private val dao: BaseDao<T>): Repository<T> {
    override fun getAll() = dao.getAll()
    override suspend fun getById(id: Int) = dao.getById(id)
    override suspend fun insert(entity: T) = dao.insert(entity)
    override suspend fun insert(entities: List<T>) = dao.insert(entities)
    override suspend fun update(entity: T) = dao.update(entity)
    override suspend fun update(entities: List<T>) = dao.update(entities)
    override suspend fun delete(entity: T) = dao.delete(entity)
    override suspend fun delete(entities: List<T>) = dao.delete(entities)

    @Transaction
    override suspend fun upsert(entity: T) = insert(entity).let {
        if (it == -1L) update(entity)
    }

    @Transaction
    override suspend fun upsert(entities: List<T>) =
        insert(entities).zip(entities).filter { it.first == -1L }.map { it.second }.let { entitiesToBeUpdated ->
            if (entitiesToBeUpdated.isNotEmpty()) update(entitiesToBeUpdated)
        }
}