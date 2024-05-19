package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo

interface ScoreboardManager {

    var scoreCarriesOver: Boolean
    var intervalList: List<Pair<ScoreInfo, IntervalData>>
    var currentIntervalIndex: Int
    val incrementList: List<List<Int>>

    fun updateScore(scoreIndex: Int, incrementIndex: Int, positive: Boolean = true)
    fun getScores(): DisplayedScoreInfo
    fun proceedToNextInterval()
    fun setTime(value:Long)
    fun getInitialTime(): Long
    fun isTimeIncreasing(): Boolean


}