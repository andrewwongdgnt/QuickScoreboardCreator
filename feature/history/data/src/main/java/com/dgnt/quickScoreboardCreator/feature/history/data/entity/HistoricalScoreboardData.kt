package com.dgnt.quickScoreboardCreator.feature.history.data.entity

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon


data class HistoricalScoreboardData(
    val historicalIntervalMap: Map<Int, HistoricalIntervalData>
)

data class HistoricalIntervalData(
    val range: HistoricalIntervalRangeData,
    val intervalLabel: IntervalLabelData,
    val historicalScoreGroupList: Map<Int, HistoricalScoreGroupData>
)

sealed class HistoricalIntervalRangeData {
    var type: String = ""

    enum class Type {
        COUNTDOWN,
        INFINITE
    }

    data class CountDown(val start: Long) : HistoricalIntervalRangeData() {
        init {
            type = Type.COUNTDOWN.name
        }
    }

    data object Infinite : HistoricalIntervalRangeData() {
        init {
            type = Type.INFINITE.name
        }
    }
}

sealed class IntervalLabelData {

    var type: String = ""
    var index: Int = -1

    enum class Type {
        CUSTOM,
        SPORT_TYPE
    }

    data class Custom(
        val value: String,

        @Transient
        val i: Int
    ) : IntervalLabelData() {
        init {
            type = Type.CUSTOM.name
            index = i
        }
    }

    data class DefaultSport(
        val sportType: SportType,

        @Transient
        val i: Int
    ) : IntervalLabelData() {
        init {
            type = Type.SPORT_TYPE.name
            index = i
        }
    }

}

data class HistoricalScoreGroupData(
    val teamLabel: TeamLabelData,
    val primaryScoreList: List<HistoricalScoreData>,
    val secondaryScoreList: List<HistoricalScoreData>,
)

sealed class TeamLabelData {
    var type: String = ""

    enum class Type {
        NONE,
        CUSTOM
    }

    data object None : TeamLabelData() {
        init {
            type = Type.NONE.name
        }
    }

    data class Custom(
        val name: String,
        val icon: TeamIcon
    ) : TeamLabelData() {
        init {
            type = Type.CUSTOM.name
        }
    }
}

data class HistoricalScoreData(
    val score: Int,
    val displayedScore: String,
    val time: Long
)