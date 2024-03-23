package com.dgnt.quickScoreboardCreator.business.scoreboard.model.score

import com.dgnt.quickScoreboardCreator.business.scoreboard.model.BaseData

data class ScoreInfo<T: BaseData<Int>>(
    val scoreRule: ScoreRule,
    val dataList: List<T>
)