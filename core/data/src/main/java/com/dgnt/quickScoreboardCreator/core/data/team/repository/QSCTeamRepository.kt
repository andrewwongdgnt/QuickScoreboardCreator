package com.dgnt.quickScoreboardCreator.core.data.team.repository

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.core.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
import javax.inject.Inject

class QSCTeamRepository @Inject constructor(
    dao: TeamDao,
    mapToDomain: Mapper<TeamEntity, TeamModel>,
    mapToEntity: Mapper<TeamModel, TeamEntity>
) : BaseRepository<TeamEntity, TeamModel>(dao, mapToDomain, mapToEntity), TeamRepository