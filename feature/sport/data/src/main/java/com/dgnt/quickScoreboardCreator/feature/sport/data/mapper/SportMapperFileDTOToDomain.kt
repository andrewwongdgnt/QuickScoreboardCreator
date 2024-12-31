package com.dgnt.quickScoreboardCreator.feature.sport.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.DefaultSportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel

class SportMapperFileDTOToDomain : Mapper<SportFileDTO, SportModel> {


    override fun map(from: SportFileDTO) =
        when (from) {
            is DefaultSportFileDTO -> SportModel(
                SportIdentifier.Default(from.sportType),
                "",
                "",
                from.winRuleType.toWinRule(),
                from.sportType.icon,
                "",
                from.intervalList.map {
                    it.scoreInfo.toScoreInfo() to it.intervalData.toIntervalData()
                }
            )

            else -> TODO("implement custom sport file dto import")
        }



}

