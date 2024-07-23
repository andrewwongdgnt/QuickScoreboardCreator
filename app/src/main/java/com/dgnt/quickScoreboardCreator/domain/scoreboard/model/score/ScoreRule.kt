package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score

sealed interface ScoreRule {
    sealed class Trigger(val trigger: Int): ScoreRule {
        class Max(trigger: Int) : Trigger(trigger)
        class DeuceAdvantage(trigger: Int) : Trigger(trigger)
    }

    data object None : ScoreRule
}