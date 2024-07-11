package com.dgnt.quickScoreboardCreator.domain.history.model

import android.os.Parcelable
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
sealed interface TeamLabel : Parcelable {
    @Serializable
    @Parcelize
    data object NoTeamLabel : TeamLabel
    @Serializable
    @Parcelize
    data class CustomTeamLabel(
        val name: String,
        val icon: TeamIcon
    ) : TeamLabel
}