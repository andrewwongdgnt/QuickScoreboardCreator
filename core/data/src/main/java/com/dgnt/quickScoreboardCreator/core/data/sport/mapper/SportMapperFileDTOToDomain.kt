package com.dgnt.quickScoreboardCreator.core.data.sport.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.DefaultSportFileDTO
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel

class SportMapperFileDTOToDomain : Mapper<SportFileDTO, SportModel> {


    override fun map(from: SportFileDTO) =
        when (from) {
            is DefaultSportFileDTO -> SportModel(
                SportIdentifier.Default(from.scoreboardType),
                "",
                "",
                from.winRuleType.toWinRule(),
                from.scoreboardType.icon,
                "",
                from.intervalList.map {
                    it.scoreInfo.toScoreInfo() to it.intervalData.toIntervalData()
                }
            )

            else -> TODO("implement custom sport file dto import")
        }



}

