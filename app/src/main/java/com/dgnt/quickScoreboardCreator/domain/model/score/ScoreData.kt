package com.dgnt.quickScoreboardCreator.domain.model.score

import com.dgnt.quickScoreboardCreator.domain.model.BaseData

data class ScoreData(
    override var current: Int,
    override val initial: Int,
    val increments: List<Int>
) : BaseData<Int>
