package com.dgnt.quickScoreboardCreator.core.domain.history.model

import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon
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