package com.dgnt.quickScoreboardCreator.feature.team.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel

class TeamMapperDomainToData : Mapper<TeamModel, TeamEntity> {

    override fun map(from: TeamModel) = TeamEntity(
        from.id,
        from.title,
        from.description,
        from.icon,
    )

}

