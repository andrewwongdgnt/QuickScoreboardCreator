package com.dgnt.quickScoreboardCreator.core.data.sport.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.sport.entity.SportEntity
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel


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

