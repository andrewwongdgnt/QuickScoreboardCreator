package com.dgnt.quickScoreboardCreator.core.data.history.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalIntervalData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalScoreData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalScoreGroupData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.IntervalLabelData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.TeamLabelData
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.core.domain.history.model.TeamLabel


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
        is IntervalLabel.Custom -> IntervalLabelData.Custom(value, index)
        is IntervalLabel.DefaultSport -> IntervalLabelData.DefaultSport(sportType, index)
    }

    private fun HistoricalScoreGroup.toData() = HistoricalScoreGroupData(
        teamLabel = teamLabel.toData(),
        primaryScoreList = primaryScoreList.map { it.toData() },
        secondaryScoreList = secondaryScoreList.map { it.toData() }
    )

    private fun TeamLabel.toData() = when (this) {
        is TeamLabel.Custom -> TeamLabelData.Custom(name, icon)
        TeamLabel.None -> TeamLabelData.None
    }

    private fun HistoricalScore.toData() = HistoricalScoreData(
        score = score,
        displayedScore = displayedScore,
        time = time
    )

}

