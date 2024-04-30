package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

sealed class ScoreboardInteractionEvent {

    data class UpdateScore(
        val scoreIndex: Int,
        val incrementIndex: Int,
        val positive: Boolean
    ) : ScoreboardInteractionEvent()
}