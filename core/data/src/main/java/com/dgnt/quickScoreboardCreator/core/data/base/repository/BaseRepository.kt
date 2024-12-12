package com.dgnt.quickScoreboardCreator.core.data.base.repository

import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseLoader
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.domain.base.repository.Repository
import kotlinx.coroutines.flow.map
import java.io.InputStream

abstract class BaseRepository<Entity, Config, Domain>(
    private val dao: BaseDao<Entity>,
    private val loader: BaseLoader<Config>,
    private val mapEntityToDomain: Mapper<Entity, Domain>,
    private val mapDomainToEntity: Mapper<Domain, Entity>,
    private val mapConfigToDomain: Mapper<Config, Domain>,
) : Repository<Domain> {
    override fun getAll() = dao.getAll().map { entities ->
        entities.map {
            mapEntityToDomain.map(it)
        }
    }

    override suspend fun getById(id: Int) = dao.getById(id)?.let { mapEntityToDomain.map(it) }
    override suspend fun insert(model: Domain) = dao.insert(mapDomainToEntity.map(model))
    override suspend fun insert(models: List<Domain>) = dao.insert(models.map { mapDomainToEntity.map(it) })
    override suspend fun delete(model: Domain) = dao.delete(mapDomainToEntity.map(model))
    override suspend fun delete(models: List<Domain>) = dao.delete(models.map { mapDomainToEntity.map(it) })
    override fun import(inputStream: InputStream) = loader.import(inputStream)?.let { mapConfigToDomain.map(it) }

}