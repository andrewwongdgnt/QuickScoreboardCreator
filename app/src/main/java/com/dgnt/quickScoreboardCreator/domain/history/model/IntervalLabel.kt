package com.dgnt.quickScoreboardCreator.domain.history.model

import android.os.Parcelable
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
sealed interface IntervalLabel : Parcelable {

    val index: Int

    @Serializable
    @Parcelize
    data class CustomIntervalLabel(val value: String, override val index: Int = -1) : IntervalLabel

    @Serializable
    @Parcelize
    data class ScoreboardTypeIntervalLabel(val scoreboardType: ScoreboardType, override val index: Int = -1) : IntervalLabel

    fun duplicateWithIndex(index: Int) =
        when (this) {
            is CustomIntervalLabel -> CustomIntervalLabel(this.value, index)
            is ScoreboardTypeIntervalLabel -> ScoreboardTypeIntervalLabel(this.scoreboardType, index)
        }

}