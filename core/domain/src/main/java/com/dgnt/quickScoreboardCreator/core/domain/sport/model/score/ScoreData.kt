package com.dgnt.quickScoreboardCreator.core.domain.sport.model.score


data class ScoreData(
    override var current: Int,
    override val initial: Int,
    val increments: List<Int>
) : com.dgnt.quickScoreboardCreator.core.domain.sport.model.BaseData<Int>
