package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData

fun interface TimeTransformer {

    operator fun invoke(milli: Long): TimeData
}