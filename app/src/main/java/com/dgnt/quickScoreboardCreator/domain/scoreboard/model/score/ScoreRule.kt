package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score

sealed interface ScoreRule {
    sealed class Trigger(val trigger: Int): ScoreRule {
        data class Max(private val _trigger: Int) : Trigger(_trigger)
        data class DeuceAdvantage(private val _trigger: Int) : Trigger(_trigger)
    }

    data object None : ScoreRule
}