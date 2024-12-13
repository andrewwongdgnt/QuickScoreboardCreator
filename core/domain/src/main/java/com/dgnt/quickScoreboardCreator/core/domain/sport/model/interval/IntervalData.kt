package com.dgnt.quickScoreboardCreator.core.domain.sport.model.interval

data class IntervalData(
    override var current: Long,
    override val initial: Long,
    val increasing: Boolean = false,
    val soundEffect: IntervalEndSound = IntervalEndSound.None
) : com.dgnt.quickScoreboardCreator.core.domain.sport.model.BaseData<Long>
