package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData

data class CustomIntervalDataConfig(
    val name: String
) : IntervalDataConfig() {
    override fun toIntervalData() =
        IntervalData(
            current,
            initial,
            increasing,
            name = name
        )
}
