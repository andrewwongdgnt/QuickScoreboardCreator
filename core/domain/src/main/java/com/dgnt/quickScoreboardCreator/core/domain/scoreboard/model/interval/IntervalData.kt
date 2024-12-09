package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.interval

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.BaseData

data class IntervalData(
    override var current: Long,
    override val initial: Long,
    val increasing: Boolean = false,
    val soundEffect: IntervalEndSound = IntervalEndSound.None
) : BaseData<Long>
