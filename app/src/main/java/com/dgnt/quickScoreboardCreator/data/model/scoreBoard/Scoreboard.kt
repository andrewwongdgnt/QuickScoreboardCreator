package com.dgnt.quickScoreboardCreator.data.model.scoreBoard

import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreInfo
import org.joda.time.DateTime

data class Scoreboard(
    var name: String,
    var description: String?,
    var createdDate: DateTime,
    var lastModifiedDate: DateTime?,
    var scoreCarriesOver: Boolean,
    var intervalList: List<Pair<ScoreInfo<ScoreData>, IntervalData>>,
    var currentIntervalIndex: Int
) {

    val currentTeamSize get() = intervalList[currentIntervalIndex].first.dataList.size

    val numberOfRounds get() = intervalList.size
}