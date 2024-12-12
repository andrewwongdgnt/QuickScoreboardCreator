package com.dgnt.quickScoreboardCreator.core.data.team.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.team.config.TeamConfig
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel

class TeamMapperConfigToDomain : Mapper<TeamConfig, TeamModel> {


    override fun map(from: TeamConfig) =
        TeamModel(
            null,
            from.title,
            from.description,
            from.icon
        )

}

