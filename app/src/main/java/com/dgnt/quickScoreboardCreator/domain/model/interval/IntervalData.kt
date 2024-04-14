package com.dgnt.quickScoreboardCreator.domain.model.interval

import com.dgnt.quickScoreboardCreator.domain.model.BaseData

data class IntervalData(
    override var current: Long,
    override val initial: Long,
    val increasing: Boolean = false
) : BaseData<Long>
