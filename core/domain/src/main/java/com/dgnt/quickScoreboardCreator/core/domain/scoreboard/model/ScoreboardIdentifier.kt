package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.ScoreboardType
import kotlinx.serialization.Serializable

@Serializable
sealed interface ScoreboardIdentifier: java.io.Serializable {
    @Serializable
    data class Custom(val id: Int) : ScoreboardIdentifier
    @Serializable
    data class Default(val scoreboardType: ScoreboardType) : ScoreboardIdentifier

}