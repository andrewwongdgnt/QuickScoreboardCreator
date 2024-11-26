package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.BaseData


data class ScoreData(
    override var current: Int,
    override val initial: Int,
    val increments: List<Int>
) : BaseData<Int>
