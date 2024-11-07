package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score

data class ScoreInfo(
    val scoreRule: ScoreRule,
    val scoreToDisplayScoreMap: Map<Int, String>,
    val secondaryScoreLabel: String,
    val dataList: List<ScoreGroup>
)