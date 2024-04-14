package com.dgnt.quickScoreboardCreator.domain.model.score

import com.dgnt.quickScoreboardCreator.domain.model.BaseData

data class ScoreInfo<T: BaseData<Int>>(
    val scoreRule: ScoreRule,
    val dataList: List<T>
)