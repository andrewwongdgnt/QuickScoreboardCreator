package com.dgnt.quickScoreboardCreator.domain.scoreboard.manager

import com.dgnt.quickScoreboardCreator.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.model.state.DisplayedScoreInfo

interface ScoreboardManager {

    var scoreCarriesOver: Boolean
    var intervalList: List<Pair<ScoreInfo<ScoreData>, IntervalData>>
    var currentIntervalIndex: Int

    fun updateScore(scoreIndex: Int, incrementIndex: Int)
    fun getScores(): DisplayedScoreInfo
    fun resetCurrentScore(scoreIndex: Int)
    fun resetCurrentScores()
    fun provideTransformMap(map: Map<Int, String>)
    fun proceedToNextInterval()
    fun restartTime()
    fun resumeTime()
    fun pauseTime()


}