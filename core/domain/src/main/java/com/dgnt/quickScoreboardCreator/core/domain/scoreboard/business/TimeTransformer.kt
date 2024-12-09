package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.time.TimeData

interface TimeTransformer {

    fun toTimeData(milli: Long): TimeData
    fun fromTimeData(timeData: TimeData): Long
}