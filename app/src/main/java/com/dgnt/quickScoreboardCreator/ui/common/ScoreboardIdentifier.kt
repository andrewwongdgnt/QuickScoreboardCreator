package com.dgnt.quickScoreboardCreator.ui.common

import android.os.Parcelable
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
sealed interface ScoreboardIdentifier : Parcelable {
    @Serializable
    @Parcelize
    data class Custom(val id: Int) : ScoreboardIdentifier
    @Serializable
    @Parcelize
    data class Default(val scoreboardType: ScoreboardType) : ScoreboardIdentifier

}