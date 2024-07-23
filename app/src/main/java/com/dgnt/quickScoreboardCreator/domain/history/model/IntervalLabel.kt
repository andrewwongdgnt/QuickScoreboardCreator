package com.dgnt.quickScoreboardCreator.domain.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
sealed interface IntervalLabel : Parcelable {

    val index: Int

    @Serializable
    @Parcelize
    data class Custom(val value: String, override val index: Int = -1) : IntervalLabel

    @Serializable
    @Parcelize
    data class ScoreboardType(val scoreboardType: com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType, override val index: Int = -1) : IntervalLabel

    fun duplicateWithIndex(index: Int) =
        when (this) {
            is Custom -> Custom(this.value, index)
            is ScoreboardType -> ScoreboardType(scoreboardType, index)
        }

}