package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData
import org.joda.time.Duration
import javax.inject.Inject

class TimeConversionUseCase @Inject constructor() {
    fun toTimeData(milli: Long): TimeData {
        val duration = Duration.millis(milli)
        val minutes = duration.standardMinutes
        val seconds = duration.standardSeconds % 60
        val centiSeconds = (milli % 1000) / 100
        return TimeData(minutes.toInt(), seconds.toInt(), centiSeconds.toInt())
    }

    fun fromTimeData(timeData: TimeData): Long {
        val minutesDuration = Duration.standardMinutes(timeData.minute.toLong())
        val secondsDuration = Duration.standardSeconds(timeData.second.toLong())
        val millisecondsDuration = Duration.standardSeconds(timeData.centiSecond.toLong() * 100)
        return minutesDuration.millis + secondsDuration.millis + millisecondsDuration.millis / 1000
    }
}