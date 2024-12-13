package com.dgnt.quickScoreboardCreator.core.data.base.repository

import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.domain.base.repository.Repository
import kotlinx.coroutines.flow.map
import java.io.InputStream

abstract class BaseRepository<Entity, FileDTO, Domain>(
    private val dao: BaseDao<Entity>,
    private val fileDao: BaseFileDao<FileDTO>,
    private val mapEntityToDomain: Mapper<Entity, Domain>,
    private val mapDomainToEntity: Mapper<Domain, Entity>,
    private val mapFileDTOToDomain: Mapper<FileDTO, Domain>,
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
    override fun import(inputStream: InputStream) = fileDao.import(inputStream)?.let { mapFileDTOToDomain.map(it) }

}