package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo

interface ScoreboardManager {

    var scoreCarriesOver: Boolean
    var intervalList: List<Pair<ScoreInfo, IntervalData>>
    val currentTeamSize: Int

    var scoresUpdateListener: ((DisplayedScoreInfo) -> Unit)?
    var timeUpdateListener: ((Long) -> Unit)?
    var intervalIndexUpdateListener: ((Int) -> Unit)?
    var incrementListUpdateListener: ((List<List<Int>>) -> Unit)?
    var teamSizeUpdateListener: ((Int) -> Unit)?

    fun triggerUpdateListeners()
    fun updateScore(scoreIndex: Int, incrementIndex: Int, positive: Boolean = true)
    fun updateTime(value:Long)
    fun updateTimeBy(value: Long)
    fun resetTime()
    fun canTimeAdvance(): Boolean
}