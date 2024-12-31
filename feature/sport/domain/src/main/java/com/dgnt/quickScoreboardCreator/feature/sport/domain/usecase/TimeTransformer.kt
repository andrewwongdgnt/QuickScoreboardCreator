package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData


interface TimeTransformer {

    fun toTimeData(milli: Long): TimeData
    fun fromTimeData(timeData: TimeData): Long
}