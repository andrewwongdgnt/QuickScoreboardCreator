package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state.DisplayedScoreInfo

interface ScoreboardManager {

    var winRule: WinRule
    var intervalList: List<Pair<ScoreInfo, IntervalData>>
    val currentTeamSize: Int

    var primaryScoresUpdateListener: ((DisplayedScoreInfo) -> Unit)?
    var secondaryScoresUpdateListener: ((DisplayedScoreInfo) -> Unit)?
    var timeUpdateListener: ((Long) -> Unit)?
    var intervalIndexUpdateListener: ((Int) -> Unit)?
    var primaryIncrementListUpdateListener: ((List<List<Int>>) -> Unit)?
    var secondaryIncrementListUpdateListener: ((List<List<Int>>) -> Unit)?
    var teamSizeUpdateListener: ((Int) -> Unit)?

    fun triggerUpdateListeners()
    fun updateScore(isPrimary: Boolean, scoreIndex: Int, incrementIndex: Int, positive: Boolean = true)
    fun updateTime(value:Long)
    fun updateTimeBy(value: Long)
    fun resetTime()
    fun canTimeAdvance(): Boolean
    fun updateInterval(index: Int)
}