package com.dgnt.quickScoreboardCreator.data.model.interval

import com.dgnt.quickScoreboardCreator.data.model.BaseData

data class TimedIntervalData(
    override var current: Long,
    override val initial: Long,
    val increasing: Boolean = false
) : BaseData<Long>
