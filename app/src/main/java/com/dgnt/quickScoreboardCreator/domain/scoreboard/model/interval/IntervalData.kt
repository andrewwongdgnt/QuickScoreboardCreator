package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.BaseData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalEndSoundType

data class IntervalData(
    override var current: Long,
    override val initial: Long,
    val increasing: Boolean = false,
    val soundEffect: IntervalEndSoundType = IntervalEndSoundType.NONE
) : BaseData<Long>
