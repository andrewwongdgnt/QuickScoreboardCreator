package com.dgnt.quickScoreboardCreator.feature.history.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreGroupData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalIntervalRange
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScore
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreGroup
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel


class HistoricalScoreboardMapperDataToDomain : Mapper<HistoricalScoreboardData, HistoricalScoreboard> {


    override fun map(from: HistoricalScoreboardData) = from.toDomain()
    private fun HistoricalScoreboardData.toDomain() = HistoricalScoreboard(
        historicalIntervalMap = historicalIntervalMap.mapValues {
            it.value.toDomain()
        }
    )

    private fun HistoricalIntervalData.toDomain() = HistoricalInterval(
        range = range.toDomain(),
        intervalLabel = intervalLabel.toDomain(),
        historicalScoreGroupList = historicalScoreGroupList.mapValues {
            it.value.toDomain()
        }
    )

    private fun HistoricalIntervalRangeData.toDomain() = when (this) {
        is HistoricalIntervalRangeData.CountDown -> HistoricalIntervalRange.CountDown(start)
        HistoricalIntervalRangeData.Infinite -> HistoricalIntervalRange.Infinite
    }

    private fun IntervalLabelData.toDomain() = when (this) {
        is IntervalLabelData.Custom -> IntervalLabel.Custom(value, index)
        is IntervalLabelData.DefaultSport -> IntervalLabel.DefaultSport(sportType, index)
    }

    private fun HistoricalScoreGroupData.toDomain() = HistoricalScoreGroup(
        teamLabel = teamLabel.toDomain(),
        primaryScoreList = primaryScoreList.map { it.toDomain() },
        secondaryScoreList = secondaryScoreList.map { it.toDomain() }
    )

    private fun TeamLabelData.toDomain() = when (this) {
        is TeamLabelData.Custom -> TeamLabel.Custom(name, icon)
        TeamLabelData.None -> TeamLabel.None
    }

    private fun HistoricalScoreData.toDomain() = HistoricalScore(
        score = score,
        displayedScore = displayedScore,
        time = time
    )

}

