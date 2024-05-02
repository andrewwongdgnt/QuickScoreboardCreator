package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

sealed class ScoreboardInteractionEvent {

    data class UpdateScore(
        val scoreIndex: Int,
        val incrementIndex: Int,
        val positive: Boolean
    ) : ScoreboardInteractionEvent()

    data object StartTimer : ScoreboardInteractionEvent()
    data class PauseTimer(val reset: Boolean) : ScoreboardInteractionEvent()
    data class SkipTime(val value: Boolean) : ScoreboardInteractionEvent()
}