package com.dgnt.quickScoreboardCreator.core.domain.history.model

import kotlinx.serialization.Serializable


@Serializable
sealed interface IntervalLabel {

    val index: Int

    @Serializable
    data class Custom(val value: String, override val index: Int = -1) : IntervalLabel

    @Serializable
    data class ScoreboardType(val scoreboardType: com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.ScoreboardType, override val index: Int = -1) : IntervalLabel

    fun duplicateWithIndex(index: Int) =
        when (this) {
            is Custom -> Custom(this.value, index)
            is ScoreboardType -> ScoreboardType(scoreboardType, index)
        }

}