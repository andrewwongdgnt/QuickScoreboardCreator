package com.dgnt.quickScoreboardCreator.data.model.scoreBoard

import com.dgnt.quickScoreboardCreator.data.model.BaseData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData

data class Scoreboard<SDT, S: ScoreData<SDT>, IDT, I: BaseData<IDT>>(
    var name: String,
    var description: String?,
    val scoreInfo: ScoreInfo<SDT, S>,
    val intervalInfo: IntervalInfo<IDT, I>


) {

    val teamSize = scoreInfo.dataList.size

    val numberOfRounds = intervalInfo.dataList.size
}