package com.dgnt.quickScoreboardCreator.data.team.repository

import com.dgnt.quickScoreboardCreator.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.data.team.dao.TeamDao
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.repository.TeamRepository

class QSCTeamRepository(dao: TeamDao) : BaseRepository<TeamEntity>(dao), TeamRepository