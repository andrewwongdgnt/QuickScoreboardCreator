package com.dgnt.quickScoreboardCreator.core.data.base.repository

import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.domain.base.repository.Repository
import kotlinx.coroutines.flow.map

abstract class BaseRepository<Data, Domain>(
    private val dao: BaseDao<Data>,
    private val mapToDomain: Mapper<Data, Domain>,
    private val mapToEntity: Mapper<Domain, Data>
) : Repository<Domain> {
    override fun getAll() = dao.getAll().map { entities ->
        entities.map {
            mapToDomain.map(it)
        }
    }

    override suspend fun getById(id: Int) = dao.getById(id)?.let { mapToDomain.map(it) }
    override suspend fun insert(model: Domain) = dao.insert(mapToEntity.map(model))
    override suspend fun insert(models: List<Domain>) = dao.insert(models.map { mapToEntity.map(it) })
    override suspend fun delete(model: Domain) = dao.delete(mapToEntity.map(model))
    override suspend fun delete(models: List<Domain>) = dao.delete(models.map { mapToEntity.map(it) })

}