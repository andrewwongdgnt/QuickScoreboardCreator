package com.dgnt.quickScoreboardCreator.domain.scoreboard.mapper

import com.dgnt.quickScoreboardCreator.domain.common.mapper.Mapper
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.Scoreboard
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.WinRuleType.COUNT
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.WinRuleType.FINAL
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.WinRuleType.SUM
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule

class ScoreboardMapperDataToDomain : Mapper<ScoreboardConfig, Scoreboard> {
    override fun map(from: ScoreboardConfig) = from.toDomain()

    private fun ScoreboardConfig.toDomain() = when (this) {
        is ScoreboardConfig.DefaultScoreboardConfig -> Scoreboard.DefaultScoreboard(
            scoreboardType = scoreboardType,
            winRule = when (winRuleType) {
                SUM -> WinRule.Sum
                FINAL -> WinRule.Final
                COUNT -> WinRule.Count
            },
            intervalList = intervalList.map {
                it.scoreInfo.toScoreInfo() to it.intervalData.toIntervalData()
            }
        )

        is ScoreboardConfig.CustomScoreboardConfig ->
    }
}