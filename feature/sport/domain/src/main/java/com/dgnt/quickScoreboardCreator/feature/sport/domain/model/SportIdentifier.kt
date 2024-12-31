package com.dgnt.quickScoreboardCreator.feature.sport.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface SportIdentifier: java.io.Serializable {
    @Serializable
    data class Custom(val id: Int) : SportIdentifier
    @Serializable
    data class Default(val sportType: SportType) : SportIdentifier

}