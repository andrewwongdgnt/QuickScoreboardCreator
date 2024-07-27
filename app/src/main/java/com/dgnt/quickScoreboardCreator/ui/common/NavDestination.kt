package com.dgnt.quickScoreboardCreator.ui.common

import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Arguments {
    const val SCOREBOARD_IDENTIFIER = "scoreboardIdentifier"
    const val ID = "id"
    const val INDEX = "index"
    const val TITLE = "title"
    const val ICON = "icon"
    const val VALUE = "value"
    const val HISTORICAL_SCOREBOARD = "historicalScoreboard"
}

@Serializable
sealed interface NavDestination {
    @Serializable
    data object ScoreboardList : NavDestination

    @Serializable
    data object TeamList : NavDestination

    @Serializable
    data object HistoryList : NavDestination

    @Serializable
    data object Contact : NavDestination

    @Serializable
    data class ScoreboardDetails(
        @SerialName(Arguments.SCOREBOARD_IDENTIFIER)
        val scoreboardIdentifier: ScoreboardIdentifier?
    ) : NavDestination

    @Serializable
    data class TeamDetails(
        @SerialName(Arguments.ID)
        val id: Int
    ) : NavDestination

    @Serializable
    data class ScoreboardInteraction(
        @SerialName(Arguments.SCOREBOARD_IDENTIFIER)
        val scoreboardIdentifier: ScoreboardIdentifier
    ) : NavDestination

    @Serializable
    data class TeamPicker(
        @SerialName(Arguments.INDEX)
        val index: Int
    ) : NavDestination

    @Serializable
    data class IntervalEditor(
        @SerialName(Arguments.VALUE)
        val value: Long,
        @SerialName(Arguments.INDEX)
        val index: Int,
        @SerialName(Arguments.SCOREBOARD_IDENTIFIER)
        val scoreboardIdentifier: ScoreboardIdentifier
    ) : NavDestination

    @Serializable
    data class TimelineViewer(
        @SerialName(Arguments.HISTORICAL_SCOREBOARD)
        val historicalScoreboard: HistoricalScoreboard,
        @SerialName(Arguments.INDEX)
        val index: Int,
        @SerialName(Arguments.TITLE)
        val title: String,
        @SerialName(Arguments.ICON)
        val icon: ScoreboardIcon,
    ) : NavDestination

    @Serializable
    data class TimelineSaver(
        @SerialName(Arguments.HISTORICAL_SCOREBOARD)
        val historicalScoreboard: HistoricalScoreboard,
        @SerialName(Arguments.TITLE)
        val title: String,
        @SerialName(Arguments.ICON)
        val icon: ScoreboardIcon,
    ) : NavDestination

    @Serializable
    data object Start : NavDestination
}