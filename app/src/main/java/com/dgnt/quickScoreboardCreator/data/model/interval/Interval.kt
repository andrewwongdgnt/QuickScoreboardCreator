package com.dgnt.quickScoreboardCreator.data.model.interval

import com.dgnt.quickScoreboardCreator.data.model.BaseData

sealed class Interval<DT, T: BaseData<DT>>(val data: T) {
    data class TimedInterval(
        val timedIntervalData: TimedIntervalData
    ) : Interval<Long, TimedIntervalData>(timedIntervalData)
}

