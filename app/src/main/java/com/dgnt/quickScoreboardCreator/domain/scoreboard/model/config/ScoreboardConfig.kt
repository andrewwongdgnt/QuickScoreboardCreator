package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule

abstract class ScoreboardConfig {
    val type: String = ""
    val winRuleType: WinRuleType = WinRuleType.FINAL
    var intervalList: List<IntervalConfig> = emptyList()
    val repeatRule: List<RepeatRuleConfig> = emptyList()
}

data class IntervalConfig(
    val scoreInfo: ScoreInfoConfig,
    val intervalData: IntervalDataConfig
)

data class RepeatRuleConfig(
    val from: Int,
    val to: List<Int>
)

data class ScoreInfoConfig(
    val scoreRule: ScoreRuleConfig,
    val scoreMapping: Map<String, String>?,
    val secondaryScoreLabel: String?,
    val dataList: List<ScoreGroupConfig>
) {
    fun toScoreInfo() =
        ScoreInfo(
            scoreRule.toScoreRule(),
            scoreMapping?.mapKeys { it.key.toInt() } ?: mapOf(),
            dataList.map {
                it.toScoreGroup()
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

data class ScoreGroupConfig(
    val primary: ScoreDataConfig,
    val secondary: ScoreDataConfig?
) {
    fun toScoreGroup() =
        ScoreGroup(
            primary.toScoreData(),
            secondary?.toScoreData(),
        )
}

data class ScoreDataConfig(
    val initial: Int,
    val increments: List<Int>
) {
    fun toScoreData() =
        ScoreData(
            initial,
            initial,
            increments
        )
}

data class IntervalDataConfig(
    val initial: Long = 0,
    val increasing: Boolean = false
) {
    fun toIntervalData() =
        IntervalData(
            initial,
            initial,
            increasing
        )
}