package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule

data class Scoreboard(
    var winRule: WinRule,
    var intervalList: List<Pair<ScoreInfo, IntervalData>>,
    var currentIntervalIndex: Int
)