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
        is IntervalLabelData.ScoreboardType -> IntervalLabel.ScoreboardType(scoreboardType, index)
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

