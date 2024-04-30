package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import kotlin.math.abs

abstract class ScoreboardConfig {
    abstract val type: String
    abstract val scoreCarriesOver: Boolean
    abstract val intervalList: List<IntervalListConfig>
}

data class IntervalListConfig(
    val scoreInfo: ScoreInfoConfig,
    val intervalData: IntervalDataConfig
)

data class ScoreInfoConfig(
    val scoreRule: ScoreRuleConfig,
    val dataList: List<ScoreDataConfig>
) {
    fun toScoreInfo() =
        ScoreInfo(
            scoreRule.toScoreRule(),
            dataList.map{
                it.toScoreData()
            }
        )
}

data class ScoreRuleConfig(
    val type: ScoreRuleType,
    val trigger: Int
) {
    fun toScoreRule() =
        when (type) {
            ScoreRuleType.NO_RULE -> ScoreRule.NoRule
            ScoreRuleType.MAX_RULE -> ScoreRule.ScoreRuleTrigger.MaxScoreRule(abs(trigger).toUInt())
            ScoreRuleType.DEUCE_ADVANTAGE -> ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule(abs(trigger).toUInt())
        }
}

data class ScoreDataConfig(
    val current: Int,
    val initial: Int,
    val increments: List<Int>
) {
    fun toScoreData() =
        ScoreData(
            current,
            initial,
            increments
        )
}

data class IntervalDataConfig(
    val current: Long,
    val initial: Long,
    val increasing: Boolean
) {
    fun toIntervalData() =
        IntervalData(
            current,
            initial,
            increasing
        )
}