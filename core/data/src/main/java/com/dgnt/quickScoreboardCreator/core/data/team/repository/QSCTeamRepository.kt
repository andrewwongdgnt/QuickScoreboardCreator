package com.dgnt.quickScoreboardCreator.core.data.team.repository

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.core.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.data.team.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
import javax.inject.Inject

class QSCTeamRepository @Inject constructor(
    dao: TeamDao,
    loader: BaseFileDao<TeamFileDTO>,
    mapEntityToDomain: Mapper<TeamEntity, TeamModel>,
    mapDomainToEntity: Mapper<TeamModel, TeamEntity>,
    mapConfigToDomain: Mapper<TeamFileDTO, TeamModel>,
) : BaseRepository<TeamEntity, TeamFileDTO, TeamModel>(dao, loader, mapEntityToDomain, mapDomainToEntity, mapConfigToDomain), TeamRepository