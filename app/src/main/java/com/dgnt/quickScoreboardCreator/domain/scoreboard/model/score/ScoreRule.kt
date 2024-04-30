package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score

sealed class ScoreRule {
    sealed class ScoreRuleTrigger(val trigger: UInt): ScoreRule() {
        class MaxScoreRule(trigger: UInt) : ScoreRuleTrigger(trigger)
        class DeuceAdvantageRule(trigger: UInt) : ScoreRuleTrigger(trigger)
    }

    data object NoRule : ScoreRule()
}