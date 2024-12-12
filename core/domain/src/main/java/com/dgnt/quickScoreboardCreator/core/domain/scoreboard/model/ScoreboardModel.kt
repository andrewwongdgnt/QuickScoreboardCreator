package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.WinRule

data class ScoreboardModel(
    val scoreboardIdentifier: ScoreboardIdentifier?,
    val title: String,
    val description: String,
    val winRule: WinRule,
    val icon: ScoreboardIcon,
    val intervalLabel: String,
    val intervalList: List<Pair<ScoreInfo, IntervalData>>
)