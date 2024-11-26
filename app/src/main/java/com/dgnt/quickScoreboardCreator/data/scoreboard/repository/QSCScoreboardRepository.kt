package com.dgnt.quickScoreboardCreator.data.scoreboard.repository

import com.dgnt.quickScoreboardCreator.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.data.scoreboard.dao.ScoreboardDao
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class QSCScoreboardRepository @Inject constructor(dao: ScoreboardDao) : BaseRepository<ScoreboardEntity>(dao), ScoreboardRepository