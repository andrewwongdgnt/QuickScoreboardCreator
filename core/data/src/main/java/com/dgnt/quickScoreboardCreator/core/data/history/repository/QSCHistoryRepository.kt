package com.dgnt.quickScoreboardCreator.core.data.history.repository

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseLoader
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.core.data.history.config.HistoryConfig
import com.dgnt.quickScoreboardCreator.core.data.history.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository

import javax.inject.Inject

class QSCHistoryRepository @Inject constructor(
    dao: HistoryDao,
    loader: BaseLoader<HistoryConfig>,
    mapEntityToDomain: Mapper<HistoryEntity, HistoryModel>,
    mapDomainToEntity: Mapper<HistoryModel, HistoryEntity>,
    mapConfigToDomain: Mapper<HistoryConfig, HistoryModel>

) : BaseRepository<HistoryEntity, HistoryConfig, HistoryModel>(dao, loader, mapEntityToDomain, mapDomainToEntity, mapConfigToDomain), HistoryRepository