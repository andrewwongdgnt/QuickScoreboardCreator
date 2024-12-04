package com.dgnt.quickScoreboardCreator.core.data.team.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel

class TeamMapperDomainToData : Mapper<TeamModel, TeamEntity> {

    override fun map(from: TeamModel) = TeamEntity(
        from.id,
        from.title,
        from.description,
        from.icon,
    )

}

