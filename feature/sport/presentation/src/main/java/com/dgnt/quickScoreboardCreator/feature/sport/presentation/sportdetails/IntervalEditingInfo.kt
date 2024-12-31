package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.ScoreInfo


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
