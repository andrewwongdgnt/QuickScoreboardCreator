package com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score


data class ScoreData(
    override var current: Int,
    override val initial: Int,
    val increments: List<Int>
) : com.dgnt.quickScoreboardCreator.feature.sport.domain.model.BaseData<Int>
