package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo

data class IntervalEditingInfo(
    val scoreInfo: ScoreInfo,
    val intervalData: IntervalData,
    val timeRepresentationPair: Pair<String,String>, //Minute and Second
    val maxScoreInput: String
)
