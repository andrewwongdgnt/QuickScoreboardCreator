package com.dgnt.quickScoreboardCreator.feature.sport.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.sport.data.entity.SportEntity
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel

class SportMapperDataToDomain : Mapper<SportEntity, SportModel> {


    override fun map(from: SportEntity) = SportModel(
        from.id?.let { SportIdentifier.Custom(it) },
        from.title,
        from.description,
        from.winRule,
        from.icon,
        from.intervalLabel,
        listOf()
    )

}

