package com.dgnt.quickScoreboardCreator.business.scoreboard.model.config

abstract class ScoreboardConfig {
    abstract val type: String
    abstract val scoreCarriesOver: Boolean
    abstract val intervalList: List<IntervalListItem>
}

data class IntervalListItem(
    val scoreInfo: ScoreInfoTemplate,
    val intervalData: IntervalDataTemplate
)

data class ScoreInfoTemplate(
    val scoreRule: ScoreRule,
    val dataList: List<ScoreDataTemplate>
)

data class ScoreRule(
    val type: ScoreRuleType,
    val trigger: Int
)

data class ScoreDataTemplate(
    val current: Int,
    val initial: Int,
    val increments: List<Int>
)

data class IntervalDataTemplate(
    val current: Int,
    val initial: Int,
    val increasing: Boolean
)