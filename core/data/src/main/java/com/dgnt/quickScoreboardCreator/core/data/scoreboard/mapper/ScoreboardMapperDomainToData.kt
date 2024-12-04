package com.dgnt.quickScoreboardCreator.core.data.scoreboard.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel


class ScoreboardMapperDomainToData : Mapper<ScoreboardModel, ScoreboardEntity> {

    override fun map(from: ScoreboardModel) = ScoreboardEntity(
        from.id,
        from.title,
        from.description,
        from.winRule,
        from.icon,
        from.intervalLabel
    )

}

