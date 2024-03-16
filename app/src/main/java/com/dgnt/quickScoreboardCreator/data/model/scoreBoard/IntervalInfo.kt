package com.dgnt.quickScoreboardCreator.data.model.scoreBoard

import com.dgnt.quickScoreboardCreator.data.model.BaseData
import com.dgnt.quickScoreboardCreator.data.model.interval.Interval

data class IntervalInfo<DT, T: BaseData<DT>>(
    val carryOver: Boolean,
    val dataList: List<Interval<DT, T>>
)
