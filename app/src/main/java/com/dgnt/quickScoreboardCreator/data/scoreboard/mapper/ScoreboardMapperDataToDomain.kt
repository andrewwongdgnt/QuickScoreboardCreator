package com.dgnt.quickScoreboardCreator.data.scoreboard.mapper

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.mapper.Mapper
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity

class ScoreboardMapperDataToDomain : Mapper<ScoreboardEntity, ScoreboardModel> {


    override fun map(from: ScoreboardEntity) = ScoreboardModel(
        from.id,
        from.title,
        from.description,
        from.winRule,
        from.icon,
        from.intervalLabel
    )

}

