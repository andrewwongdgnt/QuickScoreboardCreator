package com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.BaseData


data class ScoreData(
    override var current: Int,
    override val initial: Int,
    val increments: List<Int>
) : BaseData<Int>
