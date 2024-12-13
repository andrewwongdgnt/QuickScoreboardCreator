package com.dgnt.quickScoreboardCreator.core.data.team.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.team.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel

class TeamMapperFileDTOToDomain : Mapper<TeamFileDTO, TeamModel> {


    override fun map(from: TeamFileDTO) =
        TeamModel(
            null,
            from.title,
            from.description,
            from.icon
        )

}

