package com.dgnt.quickScoreboardCreator.business.scoreboard.model.interval

import com.dgnt.quickScoreboardCreator.business.scoreboard.model.BaseData

data class IntervalData(
    override var current: Long,
    override val initial: Long,
    val increasing: Boolean = false
) : BaseData<Long>
