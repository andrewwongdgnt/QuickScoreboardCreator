package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData

fun interface TimeTransformer {

    operator fun invoke(milli: Long): TimeData
}