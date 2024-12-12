package com.dgnt.quickScoreboardCreator.core.data.scoreboard.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel

class ScoreboardMapperConfigToDomain : Mapper<ScoreboardConfig, ScoreboardModel> {


    override fun map(from: ScoreboardConfig) =
        when (from) {
            is DefaultScoreboardConfig -> ScoreboardModel(
                ScoreboardIdentifier.Default(from.scoreboardType),
                "",
                "",
                from.winRuleType.toWinRule(),
                from.scoreboardType.icon,
                "",
                from.intervalList.map {
                    it.scoreInfo.toScoreInfo() to it.intervalData.toIntervalData()
                }
            )

            else -> TODO("implement custom scoreboard config import")
        }



}

