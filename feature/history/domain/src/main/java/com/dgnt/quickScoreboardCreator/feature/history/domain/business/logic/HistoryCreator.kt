package com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic

import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo


interface HistoryCreator {

    fun init(intervalList: List<Pair<ScoreInfo, IntervalData>>)

    fun addEntry(intervalIndex: Int, currentTime: Long, isPrimary: Boolean, scoreIndex: Int, currentScore: Int, currentDisplayedScore: String)

    fun create(intervalLabel: IntervalLabel, teamList: List<TeamLabel>): HistoricalScoreboard


}