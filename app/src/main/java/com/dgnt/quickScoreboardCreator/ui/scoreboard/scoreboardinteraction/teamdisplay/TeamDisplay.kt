package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay

import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon


sealed class TeamDisplay {
    data object UnSelectedTeamDisplay : TeamDisplay()
    data class SelectedTeamDisplay(
        val name: String,
        val teamIcon: TeamIcon
    ) : TeamDisplay()
}
