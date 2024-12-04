package com.dgnt.quickScoreboardCreator.ui.scoreboard.scoreboardinteraction.teamdisplay

import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon


sealed interface TeamDisplay {
    data object UnSelected : TeamDisplay
    data class Selected(
        val name: String,
        val icon: TeamIcon
    ) : TeamDisplay
}
