package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData

data class DefaultIntervalDataConfig(
    val intervalType: IntervalType
): IntervalDataConfig() {
    override fun toIntervalData() =
        IntervalData(
            current,
            initial,
            increasing,
            intervalType = intervalType
        )

}
