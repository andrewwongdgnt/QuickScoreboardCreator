package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.BaseData

data class ScoreInfo<T: BaseData<Int>>(
    val scoreRule: ScoreRule,
    val dataList: List<T>
)