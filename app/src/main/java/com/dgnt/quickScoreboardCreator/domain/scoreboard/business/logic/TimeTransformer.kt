package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData

interface TimeTransformer {

    fun toTimeData(milli: Long): TimeData
    fun fromTimeData(timeData: TimeData): Long
}