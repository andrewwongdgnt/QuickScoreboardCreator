package com.dgnt.quickScoreboardCreator.core.data.sport.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.sport.entity.SportEntity
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel

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

