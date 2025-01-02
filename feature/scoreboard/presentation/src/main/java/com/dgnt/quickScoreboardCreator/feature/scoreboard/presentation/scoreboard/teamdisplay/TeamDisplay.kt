package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.teamdisplay

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon


sealed interface TeamDisplay {
    data object UnSelected : TeamDisplay
    data class Selected(
        val name: String,
        val icon: TeamIcon
    ) : TeamDisplay
}
