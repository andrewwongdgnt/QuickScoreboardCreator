package com.dgnt.quickScoreboardCreator.feature.history.data.repository

import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.repository.HistoryRepository

class QSCHistoryRepository(
    dao: BaseDao<HistoryEntity>,
    fileDao: BaseFileDao<HistoryFileDTO>,
    mapEntityToDomain: Mapper<HistoryEntity, HistoryModel>,
    mapDomainToEntity: Mapper<HistoryModel, HistoryEntity>,
    mapConfigToDomain: Mapper<HistoryFileDTO, HistoryModel>

) : BaseRepository<HistoryEntity, HistoryFileDTO, HistoryModel>(dao, fileDao, mapEntityToDomain, mapDomainToEntity, mapConfigToDomain), HistoryRepository