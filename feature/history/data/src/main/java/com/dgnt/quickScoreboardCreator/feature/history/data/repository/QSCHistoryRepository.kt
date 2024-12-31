package com.dgnt.quickScoreboardCreator.feature.history.data.repository

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.feature.history.data.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.repository.HistoryRepository
import javax.inject.Inject

class QSCHistoryRepository @Inject constructor(
    dao: HistoryDao,
    loader: BaseFileDao<HistoryFileDTO>,
    mapEntityToDomain: Mapper<HistoryEntity, HistoryModel>,
    mapDomainToEntity: Mapper<HistoryModel, HistoryEntity>,
    mapConfigToDomain: Mapper<HistoryFileDTO, HistoryModel>

) : BaseRepository<HistoryEntity, HistoryFileDTO, HistoryModel>(dao, loader, mapEntityToDomain, mapDomainToEntity, mapConfigToDomain), HistoryRepository