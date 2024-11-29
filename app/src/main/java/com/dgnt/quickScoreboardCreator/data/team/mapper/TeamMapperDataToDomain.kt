package com.dgnt.quickScoreboardCreator.data.team.mapper

import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.mapper.Mapper
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity

class TeamMapperDataToDomain : Mapper<TeamEntity, TeamModel> {


    override fun map(from: TeamEntity) = TeamModel(
        from.id,
        from.title,
        from.description,
        from.icon,
    )

}

