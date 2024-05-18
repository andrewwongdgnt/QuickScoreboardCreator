package com.dgnt.quickScoreboardCreator.ui.common

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Arguments {
    const val ID = "id"
    const val TYPE = "type"
    const val INDEX = "index"
    const val VALUE = "value"
}

@Serializable
sealed class NavDestination {
    @Serializable
    data object ScoreboardList : NavDestination()

    @Serializable
    data object TeamList : NavDestination()

    @Serializable
    data object Contact : NavDestination()

    @Serializable
    data class ScoreboardDetails(
        @SerialName(Arguments.ID)
        val id: Int,
        @SerialName(Arguments.TYPE)
        val type: ScoreboardType
    ) : NavDestination()

    @Serializable
    data class TeamDetails(
        @SerialName(Arguments.ID)
        val id: Int
    ) : NavDestination()

    @Serializable
    data class ScoreboardInteraction(
        @SerialName(Arguments.ID)
        val id: Int,
        @SerialName(Arguments.TYPE)
        val type: ScoreboardType
    ) : NavDestination()

    @Serializable
    data class TeamPicker(
        @SerialName(Arguments.INDEX)
        val index: Int
    ) : NavDestination()

    @Serializable
    data class IntervalEditor(
        @SerialName(Arguments.VALUE)
        val value: Long,
        @SerialName(Arguments.INDEX)
        val index: Int,
        @SerialName(Arguments.ID)
        val id: Int,
        @SerialName(Arguments.TYPE)
        val type: ScoreboardType
    ) : NavDestination()

    @Serializable
    data object Start : NavDestination()
}