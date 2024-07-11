package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay

import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon


sealed interface TeamDisplay {
    data object UnSelectedTeamDisplay : TeamDisplay
    data class SelectedTeamDisplay(
        val name: String,
        val icon: TeamIcon
    ) : TeamDisplay
}
