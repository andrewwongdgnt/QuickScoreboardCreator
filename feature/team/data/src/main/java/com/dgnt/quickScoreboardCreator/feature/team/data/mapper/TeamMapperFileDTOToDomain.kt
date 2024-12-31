package com.dgnt.quickScoreboardCreator.feature.team.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel

class TeamMapperFileDTOToDomain : Mapper<TeamFileDTO, TeamModel> {


    override fun map(from: TeamFileDTO) =
        TeamModel(
            null,
            from.title,
            from.description,
            from.icon
        )

}

