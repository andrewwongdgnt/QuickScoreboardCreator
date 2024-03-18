package com.dgnt.quickScoreboardCreator.data.model.score

import com.dgnt.quickScoreboardCreator.data.model.BaseData

data class ScoreInfo<T: BaseData<Int>>(
    val scoreRule: ScoreRule,
    val dataList: List<T>
)