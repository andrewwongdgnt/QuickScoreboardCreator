package com.dgnt.quickScoreboardCreator.feature.history.domain.model

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import kotlinx.serialization.Serializable


@Serializable
sealed interface TeamLabel {
    @Serializable
    data object None : TeamLabel
    @Serializable
    data class Custom(
        val name: String,
        val icon: TeamIcon
    ) : TeamLabel
}