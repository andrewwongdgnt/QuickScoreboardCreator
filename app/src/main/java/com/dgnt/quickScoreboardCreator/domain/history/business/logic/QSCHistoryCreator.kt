package com.dgnt.quickScoreboardCreator.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo

class QSCHistoryCreator : HistoryCreator {

    private val allEntries = mutableListOf<HistoryEntry>()

    private var intervalList = listOf<Pair<ScoreInfo, IntervalData>>()

    override fun init(intervalList: List<Pair<ScoreInfo, IntervalData>>) {
        allEntries.clear()
        this.intervalList = intervalList
    }

    override fun addEntry(
        intervalIndex: Int,
        currentTime: Long,
        isPrimary: Boolean,
        scoreIndex: Int,
        currentScore: Int,
        currentDisplayedScore: String
    ) {
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

    override fun create(
        intervalLabel: IntervalLabel,
        teamList: List<TeamLabel>
    ): HistoricalScoreboard {
        return allEntries
            .groupBy { it.intervalIndex }
            .mapValues { entry ->
                entry.value.groupBy { it.scoreIndex }
                    .mapValues { historyEntries ->
                        val scoreIndex = historyEntries.key
                        val mapHistoryEntry: (HistoryEntry) -> HistoricalScore = {
                            HistoricalScore(it.currentScore, it.currentDisplayedScore, it.currentTime)
                        }
                        HistoricalScoreGroup(
                            teamList.getOrNull(scoreIndex) ?: TeamLabel.None,
                            historyEntries.value.filter { it.isPrimary }.map(mapHistoryEntry),
                            historyEntries.value.filterNot { it.isPrimary }.sortedBy { it.currentTime }.map(mapHistoryEntry)
                        )
                    }.let {
                        val intervalIndex = entry.key
                        HistoricalInterval(
                            range = getHistoricalIntervalRange(intervalIndex),
                            intervalLabel = intervalLabel.duplicateWithIndex(intervalIndex),
                            historicalScoreGroupList = it
                        )
                    }
            }.let {
                HistoricalScoreboard(it)
            }
    }

    private fun getHistoricalIntervalRange(index: Int): HistoricalIntervalRange {
        val intervalData = intervalList[index].second
        return if (intervalData.increasing)
            HistoricalIntervalRange.Infinite
        else {
            HistoricalIntervalRange.CountDown(intervalData.initial)
        }
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