package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import org.joda.time.Duration

class QSCTimeTransformer : TimeTransformer {
    override fun invoke(milli: Long): TimeData {
        val duration = Duration.millis(milli)
        val minutes = duration.standardMinutes % 60
        val seconds = duration.standardSeconds % 60
        val centiSeconds = (milli % 1000) / 100
        return TimeData(minutes, seconds, centiSeconds)
    }
}