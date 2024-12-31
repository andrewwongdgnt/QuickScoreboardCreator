package com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business



import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule


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
    var winnersUpdateListener: ((Set<Int>) -> Unit)?
    var intervalOnEndListener: ((IntervalEndSound) -> Unit)?

    fun triggerUpdateListeners()
    fun updateScore(isPrimary: Boolean, scoreIndex: Int, incrementIndex: Int, main: Boolean = true)
    fun updateTime(value: Long)
    fun updateTimeBy(value: Long)
    fun resetTime()
    fun canTimeAdvance(): Boolean
    fun updateInterval(index: Int)
    fun createTimeline(intervalLabel: IntervalLabel, teamList: List<TeamLabel>): HistoricalScoreboard
}