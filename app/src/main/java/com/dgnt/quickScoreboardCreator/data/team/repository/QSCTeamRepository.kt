package com.dgnt.quickScoreboardCreator.data.team.repository

import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
import com.dgnt.quickScoreboardCreator.core.mapper.Mapper
import com.dgnt.quickScoreboardCreator.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import javax.inject.Inject

class QSCTeamRepository @Inject constructor(
    dao: TeamDao,
    mapToDomain: Mapper<TeamEntity, TeamModel>,
    mapToEntity: Mapper<TeamModel, TeamEntity>
) : BaseRepository<TeamEntity, TeamModel>(dao, mapToDomain, mapToEntity), TeamRepository