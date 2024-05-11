package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo

data class Scoreboard(
    var scoreCarriesOver: Boolean,
    var intervalList: List<Pair<ScoreInfo, IntervalData>>,
    var currentIntervalIndex: Int
)