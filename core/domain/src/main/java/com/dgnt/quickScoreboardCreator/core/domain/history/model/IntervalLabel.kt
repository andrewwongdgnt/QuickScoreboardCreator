package com.dgnt.quickScoreboardCreator.core.domain.history.model

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportType
import kotlinx.serialization.Serializable


@Serializable
sealed interface IntervalLabel {

    val index: Int

    @Serializable
    data class Custom(val value: String, override val index: Int = -1) : IntervalLabel

    @Serializable
    data class DefaultSport(val sportType: SportType, override val index: Int = -1) : IntervalLabel

    fun duplicateWithIndex(index: Int) =
        when (this) {
            is Custom -> Custom(this.value, index)
            is DefaultSport -> DefaultSport(sportType, index)
        }

}