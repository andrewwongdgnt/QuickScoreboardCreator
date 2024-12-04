package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.WinRule

data class ScoreboardModel (
    val id: Int? = null,
    val title: String,
    val description: String,
    val winRule: WinRule,
    val icon: ScoreboardIcon,
    val intervalLabel: String
)