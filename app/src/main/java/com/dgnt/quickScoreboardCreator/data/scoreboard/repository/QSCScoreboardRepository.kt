package com.dgnt.quickScoreboardCreator.data.scoreboard.repository

import com.dgnt.quickScoreboardCreator.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.data.scoreboard.dao.ScoreboardDao
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository

class QSCScoreboardRepository(dao: ScoreboardDao) : BaseRepository<ScoreboardEntity>(dao), ScoreboardRepository