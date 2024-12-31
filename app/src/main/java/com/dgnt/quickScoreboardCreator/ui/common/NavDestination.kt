package com.dgnt.quickScoreboardCreator.ui.common

import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
sealed interface NavDestination {
    @Serializable
    data object SportList : NavDestination

    @Serializable
    data object TeamList : NavDestination

    @Serializable
    data object HistoryList : NavDestination

    @Serializable
    data object Contact : NavDestination

    @Serializable
    data class SportDetails(
        @SerialName(NavArguments.SPORT_IDENTIFIER)
        val sportIdentifier: SportIdentifier?
    ) : NavDestination

    @Serializable
    data class TeamDetails(
        @SerialName(NavArguments.ID)
        val id: Int
    ) : NavDestination

    @Serializable
    data class HistoryDetails(
        @SerialName(NavArguments.ID)
        val id: Int,
    ) : NavDestination

    @Serializable
    data class Scoreboard(
        @SerialName(NavArguments.SPORT_IDENTIFIER)
        val sportIdentifier: SportIdentifier
    ) : NavDestination

    @Serializable
    data class TeamPicker(
        @SerialName(NavArguments.INDEX)
        val index: Int
    ) : NavDestination

    @Serializable
    data class IntervalEditor(
        @SerialName(NavArguments.VALUE)
        val value: Long,
        @SerialName(NavArguments.INDEX)
        val index: Int,
        @SerialName(NavArguments.SPORT_IDENTIFIER)
        val sportIdentifier: SportIdentifier
    ) : NavDestination

    @Serializable
    data class TimelineViewer(
        @SerialName(NavArguments.ID)
        val id: Int,
        @SerialName(NavArguments.INDEX)
        val index: Int,
    ) : NavDestination

    @Serializable
    data object Start : NavDestination
}