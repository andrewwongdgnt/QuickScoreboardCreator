package com.dgnt.quickScoreboardCreator.core.domain.sport.usecase

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.time.TimeData


interface TimeTransformer {

    fun toTimeData(milli: Long): TimeData
    fun fromTimeData(timeData: TimeData): Long
}