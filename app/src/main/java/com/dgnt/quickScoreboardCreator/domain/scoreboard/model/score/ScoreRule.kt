package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score

sealed interface ScoreRule {
    sealed class ScoreRuleTrigger(val trigger: Int): ScoreRule {
        class MaxScoreRule(trigger: Int) : ScoreRuleTrigger(trigger)
        class DeuceAdvantageRule(trigger: Int) : ScoreRuleTrigger(trigger)
    }

    data object NoRule : ScoreRule
}