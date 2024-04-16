package com.dgnt.quickScoreboardCreator.data.repository

import com.dgnt.quickScoreboardCreator.data.dao.ScoreboardDao
import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.repository.ScoreboardRepository

class QSCScoreboardRepository(dao: ScoreboardDao) : BaseRepository<ScoreboardEntity>(dao), ScoreboardRepository