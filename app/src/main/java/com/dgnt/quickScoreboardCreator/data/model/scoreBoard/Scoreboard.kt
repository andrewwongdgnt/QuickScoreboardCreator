package com.dgnt.quickScoreboardCreator.data.model.scoreBoard

import com.dgnt.quickScoreboardCreator.data.model.BaseData
import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalInfo
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreInfo
import org.joda.time.DateTime

data class Scoreboard(
    var name: String,
    var description: String?,
    var createdDate: DateTime,
    var lastModifiedDate: DateTime?,
    var scoreInfo: ScoreInfo<ScoreData>,
    var intervalInfo: IntervalInfo<IntervalData>


) {

    val teamSize get() = scoreInfo.dataList.size

    val numberOfRounds get() = intervalInfo.dataList.size
}