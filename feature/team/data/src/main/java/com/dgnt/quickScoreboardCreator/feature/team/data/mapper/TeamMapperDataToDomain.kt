package com.dgnt.quickScoreboardCreator.feature.team.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel

class TeamMapperDataToDomain : Mapper<TeamEntity, TeamModel> {


    override fun map(from: TeamEntity) = TeamModel(
        from.id,
        from.title,
        from.description,
        from.icon,
    )

}

