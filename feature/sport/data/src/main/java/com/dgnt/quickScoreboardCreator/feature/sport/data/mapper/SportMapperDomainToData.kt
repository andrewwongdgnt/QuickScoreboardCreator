package com.dgnt.quickScoreboardCreator.feature.sport.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.sport.data.entity.SportEntity
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel


class SportMapperDomainToData : Mapper<SportModel, SportEntity> {

    override fun map(from: SportModel) = SportEntity(
        (from.sportIdentifier as? SportIdentifier.Custom)?.id,
        from.title,
        from.description,
        from.winRule,
        from.icon,
        from.intervalLabel
    )

}

