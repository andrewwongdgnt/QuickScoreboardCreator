package com.dgnt.quickScoreboardCreator.data.history.repository

import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository
import com.dgnt.quickScoreboardCreator.core.mapper.Mapper
import com.dgnt.quickScoreboardCreator.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.data.history.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import javax.inject.Inject

class QSCHistoryRepository @Inject constructor(
    dao: HistoryDao,
    mapToDomain: Mapper<HistoryEntity, HistoryModel>,
    mapToEntity: Mapper<HistoryModel, HistoryEntity>

) : BaseRepository<HistoryEntity, HistoryModel>(dao, mapToDomain, mapToEntity), HistoryRepository