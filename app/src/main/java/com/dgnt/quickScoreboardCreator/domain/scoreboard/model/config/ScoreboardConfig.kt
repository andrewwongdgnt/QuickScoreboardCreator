package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

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
    val scoreRule: ScoreRule,
    val dataList: List<ScoreDataConfig>
)

data class ScoreRule(
    val type: ScoreRuleType,
    val trigger: Int
)

data class ScoreDataConfig(
    val current: Int,
    val initial: Int,
    val increments: List<Int>
)

data class IntervalDataConfig(
    val current: Int,
    val initial: Int,
    val increasing: Boolean
)