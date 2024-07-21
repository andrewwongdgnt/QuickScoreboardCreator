package com.dgnt.quickScoreboardCreator.data.history.data

import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon

data class HistoricalScoreboardData(
    val historicalIntervalMap: Map<Int, HistoricalIntervalData>
)

data class HistoricalIntervalData(
    val range: HistoricalIntervalRangeData,
    val intervalLabel: IntervalLabelData,
    val historicalScoreGroupList: Map<Int, HistoricalScoreGroupData>
)

abstract class HistoricalIntervalRangeData {
    open val type: String = ""

    enum class Type {
        COUNTDOWN,
        INFINITE
    }

    data class CountDown(val start: Long) : HistoricalIntervalRangeData() {

        @Transient
        override val type: String = Type.COUNTDOWN.name
    }

    data object Infinite : HistoricalIntervalRangeData() {

        @Transient
        override val type: String = Type.INFINITE.name
    }
}

abstract class IntervalLabelData {

    open val type: String = ""
    open val index: Int = -1

    enum class Type {
        CUSTOM,
        SCOREBOARD_TYPE
    }


    data class Custom(
        val value: String,

        @Transient
        override val index: Int
    ) : IntervalLabelData() {

        @Transient
        override val type: String = Type.CUSTOM.name
    }


    data class ScoreboardType(
        val scoreboardType: com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType,

        @Transient
        override val index: Int
    ) : IntervalLabelData() {

        @Transient
        override val type: String = Type.SCOREBOARD_TYPE.name
    }

}

data class HistoricalScoreGroupData(
    val teamLabel: TeamLabelData,
    val primaryScoreList: List<HistoricalScoreData>,
    val secondaryScoreList: List<HistoricalScoreData>,
)

abstract class TeamLabelData {
    open val type: String = ""

    enum class Type {
        NONE,
        CUSTOM
    }

    data object None : TeamLabelData() {
        @Transient
        override val type = Type.NONE.name
    }

    data class Custom(
        val name: String,
        val icon: TeamIcon
    ) : TeamLabelData() {
        @Transient
        override val type = Type.CUSTOM.name
    }
}

data class HistoricalScoreData(
    val score: Int,
    val displayedScore: String,
    val time: Long
)