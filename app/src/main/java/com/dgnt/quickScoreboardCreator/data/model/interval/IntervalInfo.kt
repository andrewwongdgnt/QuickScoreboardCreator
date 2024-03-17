package com.dgnt.quickScoreboardCreator.data.model.interval

import com.dgnt.quickScoreboardCreator.data.model.BaseData

data class IntervalInfo<T: BaseData<Long>>(
    val carryOver: Boolean,
    val dataList: List<T>
)
