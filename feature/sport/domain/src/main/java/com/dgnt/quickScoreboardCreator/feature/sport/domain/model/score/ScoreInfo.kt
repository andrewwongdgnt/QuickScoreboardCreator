package com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score

data class ScoreInfo(
    val scoreRule: ScoreRule,
    val scoreToDisplayScoreMap: Map<Int, String>,
    val secondaryScoreLabel: String,
    val dataList: List<ScoreGroup>
)