package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction

import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedIntervalData
import com.dgnt.quickScoreboardCreator.ui.scoreboard.UpdatedTeamData

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
    data class UpdatedTeam(
        val updatedTeamData: UpdatedTeamData
    ) : ScoreboardInteractionEvent()

    data object UpdateInterval : ScoreboardInteractionEvent()
    data class UpdatedInterval(
        val updatedIntervalData: UpdatedIntervalData
    ) : ScoreboardInteractionEvent()
}