package com.dgnt.quickScoreboardCreator.core.data.team.repository

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseLoader
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.core.data.team.config.TeamConfig
import com.dgnt.quickScoreboardCreator.core.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
import javax.inject.Inject

class QSCTeamRepository @Inject constructor(
    dao: TeamDao,
    loader: BaseLoader<TeamConfig>,
    mapEntityToDomain: Mapper<TeamEntity, TeamModel>,
    mapDomainToEntity: Mapper<TeamModel, TeamEntity>,
    mapConfigToDomain: Mapper<TeamConfig, TeamModel>,
) : BaseRepository<TeamEntity, TeamConfig, TeamModel>(dao, loader, mapEntityToDomain, mapDomainToEntity, mapConfigToDomain), TeamRepository