package com.dgnt.quickScoreboardCreator.ui.scoreboard

sealed class ScoreboardActivityEvent {
    data class OnUpdatedIntervalData(val updatedIntervalData: UpdatedIntervalData?) : ScoreboardActivityEvent()
    data class OnUpdatedTeamData(val updatedTeamData: UpdatedTeamData?) : ScoreboardActivityEvent()
}