package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.BaseData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalType

data class IntervalData(
    override var current: Long,
    override val initial: Long,
    val increasing: Boolean = false,
    val intervalType: IntervalType? = null,
    val name: String? = null

) : BaseData<Long>
