package com.dgnt.quickScoreboardCreator.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard

interface HistoryCreator {

    fun init()

    fun addEntry(intervalIndex: Int, currentTime: Long, isPrimary: Boolean, scoreIndex: Int, currentScore: Int, currentDisplayedScore: String)

    fun create(): HistoricalScoreboard
}