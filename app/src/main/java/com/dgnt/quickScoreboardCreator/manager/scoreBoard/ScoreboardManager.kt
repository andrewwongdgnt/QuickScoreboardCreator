package com.dgnt.quickScoreboardCreator.manager.scoreBoard

import com.dgnt.quickScoreboardCreator.data.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.data.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.data.model.state.DisplayedScoreInfo
import org.joda.time.DateTime

interface ScoreboardManager {
    var name: String
    var description: String?
    var createdDate: DateTime
    var lastModifiedDate: DateTime?
    var scoreCarriesOver: Boolean
    var intervalList: List<Pair<ScoreInfo<ScoreData>, IntervalData>>
    var currentIntervalIndex: Int

    fun updateScore(scoreIndex: Int, incrementIndex: Int)
    fun getScores(): DisplayedScoreInfo
    fun resetScoreAtCurrentInterval(scoreIndex: Int)
    fun resetCurrentScores()
    fun provideTransformMap(map: Map<Int, String>)
}