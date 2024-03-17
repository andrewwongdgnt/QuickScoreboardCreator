package com.dgnt.quickScoreboardCreator.data.model.score

import com.dgnt.quickScoreboardCreator.data.model.BaseData

data class ScoreData(
    override var current: Int,
    override val initial: Int,
    val increments: List<Int>
) : BaseData<Int>
