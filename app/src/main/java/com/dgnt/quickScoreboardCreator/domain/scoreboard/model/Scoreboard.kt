package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo

data class Scoreboard(
    var scoreCarriesOver: Boolean,
    var intervalList: List<Pair<ScoreInfo<ScoreData>, IntervalData>>,
    var currentIntervalIndex: Int
) {

    val currentTeamSize get() = intervalList[currentIntervalIndex].first.dataList.size

    val numberOfIntervals get() = intervalList.size
}