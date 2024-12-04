package com.dgnt.quickScoreboardCreator.core.data.scoreboard.repository

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.repository.ScoreboardRepository
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.dao.ScoreboardDao
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.entity.ScoreboardEntity
import javax.inject.Inject

class QSCScoreboardRepository @Inject constructor(
    dao: ScoreboardDao,
    mapToDomain: Mapper<ScoreboardEntity, ScoreboardModel>,
    mapToEntity: Mapper<ScoreboardModel, ScoreboardEntity>
) : BaseRepository<ScoreboardEntity, ScoreboardModel>(dao, mapToDomain, mapToEntity), ScoreboardRepository