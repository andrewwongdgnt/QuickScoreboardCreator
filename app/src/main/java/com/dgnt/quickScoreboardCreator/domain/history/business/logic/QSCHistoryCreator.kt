package com.dgnt.quickScoreboardCreator.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard

class QSCHistoryCreator : HistoryCreator {

    private val allEntries = mutableListOf<HistoryEntry>()
    override fun addEntry(intervalIndex: Int, currentTime: Long, isPrimary: Boolean, scoreIndex: Int, currentScore: Int, currentDisplayedScore: String) {
        allEntries.add(
            HistoryEntry(
                intervalIndex,
                currentTime,
                isPrimary,
                scoreIndex,
                currentScore,
                currentDisplayedScore
            )
        )
    }

    override fun create() =
        allEntries
            .groupBy { it.intervalIndex }
            .mapValues { entry ->
                entry.value.groupBy { it.scoreIndex }
                    .mapValues { historyEntries ->

                        val mapHistoryEntry: (HistoryEntry) -> HistoricalScore = {
                            HistoricalScore(it.currentScore, it.currentDisplayedScore, it.currentTime)
                        }
                        HistoricalScoreGroup(
                            historyEntries.value.filter { it.isPrimary }.map(mapHistoryEntry),
                            historyEntries.value.filterNot { it.isPrimary }.sortedBy { it.currentTime }.map(mapHistoryEntry)
                        )
                    }.let {
                        HistoricalInterval(it)
                    }
            }.let {
                HistoricalScoreboard(it)
            }


    private data class HistoryEntry(
        val intervalIndex: Int,
        val currentTime: Long,
        val isPrimary: Boolean,
        val scoreIndex: Int,
        val currentScore: Int,
        val currentDisplayedScore: String
    )


}