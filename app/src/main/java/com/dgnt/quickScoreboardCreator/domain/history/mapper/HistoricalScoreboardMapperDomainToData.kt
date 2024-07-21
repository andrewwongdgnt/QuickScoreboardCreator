package com.dgnt.quickScoreboardCreator.domain.history.mapper

import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalIntervalData
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreData
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreGroupData
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.data.IntervalLabelData
import com.dgnt.quickScoreboardCreator.data.history.data.TeamLabelData
import com.dgnt.quickScoreboardCreator.domain.common.mapper.Mapper
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.domain.history.model.TeamLabel

class HistoricalScoreboardMapperDomainToData : Mapper<HistoricalScoreboard, HistoricalScoreboardData> {


    override fun map(from: HistoricalScoreboard) = from.toData()
    private fun HistoricalScoreboard.toData() = HistoricalScoreboardData(
        historicalIntervalMap = historicalIntervalMap.mapValues {
            it.value.toData()
        }
    )

    private fun HistoricalInterval.toData() = HistoricalIntervalData(
        range = range.toData(),
        intervalLabel = intervalLabel.toData(),
        historicalScoreGroupList = historicalScoreGroupList.mapValues {
            it.value.toData()
        }
    )

    private fun HistoricalIntervalRange.toData() = when (this) {
        is HistoricalIntervalRange.CountDown -> HistoricalIntervalRangeData.CountDown(start)
        HistoricalIntervalRange.Infinite -> HistoricalIntervalRangeData.Infinite
    }

    private fun IntervalLabel.toData() = when (this) {
        is IntervalLabel.CustomIntervalLabel -> IntervalLabelData.Custom(value, index)
        is IntervalLabel.ScoreboardTypeIntervalLabel -> IntervalLabelData.ScoreboardType(scoreboardType, index)
    }

    private fun HistoricalScoreGroup.toData() = HistoricalScoreGroupData(
        teamLabel = teamLabel.toData(),
        primaryScoreList = primaryScoreList.map { it.toData() },
        secondaryScoreList = secondaryScoreList.map { it.toData() }
    )

    private fun TeamLabel.toData() = when (this) {
        is TeamLabel.CustomTeamLabel -> TeamLabelData.Custom(name, icon)
        TeamLabel.NoTeamLabel -> TeamLabelData.None
    }

    private fun HistoricalScore.toData() = HistoricalScoreData(
        score = score,
        displayedScore = displayedScore,
        time = time
    )

}

