package com.dgnt.quickScoreboardCreator.manager.scoreBoard

import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalInfo
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.data.model.state.DisplayedScoreInfo
import org.joda.time.DateTime

interface ScoreboardManager {
    var name: String
    var description: String?
    var createdDate: DateTime
    var lastModifiedDate: DateTime?
    var scoreInfo: ScoreInfo<ScoreData>
    var intervalInfo: IntervalInfo<IntervalData>

    fun updateScore(scoreIndex: Int, incrementIndex: Int)
    fun getScores(): DisplayedScoreInfo
    fun reset(scoreIndex: Int? = null, intervalIndex: Int? = null)
    fun resetIntervalAndAllScore(intervalIndex: Int)
    fun provideTransformMap(map: Map<Int, String>)
}