package com.dgnt.quickScoreboardCreator.core.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.core.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.ScoreInfo


interface HistoryCreator {

    fun init(intervalList: List<Pair<ScoreInfo, IntervalData>>)

    fun addEntry(intervalIndex: Int, currentTime: Long, isPrimary: Boolean, scoreIndex: Int, currentScore: Int, currentDisplayedScore: String)

    fun create(intervalLabel: IntervalLabel, teamList: List<TeamLabel>): HistoricalScoreboard


}