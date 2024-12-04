package com.dgnt.quickScoreboardCreator.ui.common

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIdentifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Arguments {
    const val SCOREBOARD_IDENTIFIER = "scoreboardIdentifier"
    const val TIMELINE_VIEWER_IDENTIFIER = "timelineViewerIdentifier"
    const val ID = "id"
    const val INDEX = "index"
    const val VALUE = "value"
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
    data class HistoryDetails(
        @SerialName(Arguments.ID)
        val id: Int,
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
        @SerialName(Arguments.ID)
        val id: Int,
        @SerialName(Arguments.INDEX)
        val index: Int,
    ) : NavDestination

    @Serializable
    data object Start : NavDestination
}