package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import com.dgnt.quickScoreboardCreator.ui.scoreboard.TeamSelectedData

sealed class ScoreboardInteractionEvent {

    data class UpdateScore(
        val scoreIndex: Int,
        val incrementIndex: Int,
        val positive: Boolean
    ) : ScoreboardInteractionEvent()

    data object StartTimer : ScoreboardInteractionEvent()
    data class PauseTimer(val reset: Boolean) : ScoreboardInteractionEvent()

    data class UpdateTeam(
        val scoreIndex: Int
    ) : ScoreboardInteractionEvent()
    data class SetTeam(
        val teamSelectedData: TeamSelectedData
    ) : ScoreboardInteractionEvent()
}