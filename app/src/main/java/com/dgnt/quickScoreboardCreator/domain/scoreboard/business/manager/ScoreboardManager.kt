package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo

interface ScoreboardManager {

    var scoreCarriesOver: Boolean
    var intervalList: List<Pair<ScoreInfo<ScoreData>, IntervalData>>
    var currentIntervalIndex: Int
    val incrementList: List<List<Int>>

    fun updateScore(scoreIndex: Int, incrementIndex: Int, positive: Boolean = true)
    fun getScores(): DisplayedScoreInfo
    fun resetCurrentScore(scoreIndex: Int)
    fun resetCurrentScores()
    fun provideTransformMap(map: Map<Int, String>)
    fun proceedToNextInterval()
    fun restartTime()
    fun resumeTime()
    fun pauseTime()


}