package com.dgnt.quickScoreboardCreator.data.history.repository

import com.dgnt.quickScoreboardCreator.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.data.history.dao.HistoryDao
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.history.repository.HistoryRepository
import javax.inject.Inject

class QSCHistoryRepository @Inject constructor(dao: HistoryDao) : BaseRepository<HistoryEntity>(dao), HistoryRepository