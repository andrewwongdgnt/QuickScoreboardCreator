package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule

abstract class ScoreboardConfig {
    val type: String = ""
    val scoreCarriesOver: Boolean = false
    val intervalList: List<IntervalConfig> = emptyList()
}

data class IntervalConfig(
    val scoreInfo: ScoreInfoConfig,
    val intervalData: IntervalDataConfig
)

data class ScoreInfoConfig(
    val scoreRule: ScoreRuleConfig,
    val scoreMapping: Map<String, String>?,
    val dataList: List<ScoreDataConfig>
) {
    fun toScoreInfo() =
        ScoreInfo(
            scoreRule.toScoreRule(),
            scoreMapping?.mapKeys { it.key.toInt() } ?: mapOf(),
            dataList.map {
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
            ScoreRuleType.MAX_RULE -> ScoreRule.ScoreRuleTrigger.MaxScoreRule(trigger)
            ScoreRuleType.DEUCE_ADVANTAGE -> ScoreRule.ScoreRuleTrigger.DeuceAdvantageRule(trigger)
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
    val current: Long = 0,
    val initial: Long = 0,
    val increasing: Boolean = false
) {
    fun toIntervalData() =
        IntervalData(
            current,
            initial,
            increasing
        )
}