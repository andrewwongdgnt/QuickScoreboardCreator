package com.dgnt.quickScoreboardCreator.core.data.scoreboard.repository

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseLoader
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.dao.ScoreboardDao
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class QSCScoreboardRepository @Inject constructor(
    dao: ScoreboardDao,
    loader: BaseLoader<ScoreboardConfig>,
    mapEntityToDomain: Mapper<ScoreboardEntity, ScoreboardModel>,
    mapDomainToEntity: Mapper<ScoreboardModel, ScoreboardEntity>,
    mapConfigToDomain: Mapper<ScoreboardConfig, ScoreboardModel>
) : BaseRepository<ScoreboardEntity, ScoreboardConfig, ScoreboardModel>(dao, loader, mapEntityToDomain, mapDomainToEntity, mapConfigToDomain), ScoreboardRepository