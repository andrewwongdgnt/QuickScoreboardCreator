package com.dgnt.quickScoreboardCreator.core.data.history.repository

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.core.data.history.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository

import javax.inject.Inject

class QSCHistoryRepository @Inject constructor(
    dao: HistoryDao,
    mapToDomain: Mapper<HistoryEntity, HistoryModel>,
    mapToEntity: Mapper<HistoryModel, HistoryEntity>

) : BaseRepository<HistoryEntity, HistoryModel>(dao, mapToDomain, mapToEntity), HistoryRepository