package com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.BaseData

data class IntervalData(
    override var current: Long,
    override val initial: Long,
    val increasing: Boolean = false,
    val soundEffect: IntervalEndSound = IntervalEndSound.None
) : BaseData<Long>
