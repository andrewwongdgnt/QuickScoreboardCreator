package com.dgnt.quickScoreboardCreator.core.domain.sport.model

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule

data class SportModel(
    val sportIdentifier: SportIdentifier?,
    val title: String,
    val description: String,
    val winRule: WinRule,
    val icon: SportIcon,
    val intervalLabel: String,
    val intervalList: List<Pair<ScoreInfo, IntervalData>>
)