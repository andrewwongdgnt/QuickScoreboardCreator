package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreInfo

data class IntervalEditingInfo(
    val scoreInfo: ScoreInfo,
    val intervalData: IntervalData,
    val timeRepresentationPair: Pair<String,String>, //Minute and Second
    val maxScoreInput: String,
    val initialScoreInput: String,
    val primaryIncrementInputList: List<String>,
    val allowPrimaryMapping: Boolean,
    val primaryMappingInputList: List<Pair<String, String>>,
    val allowSecondaryScore: Boolean,

)
