package com.dgnt.quickScoreboardCreator.ui.common

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType

sealed class UiEvent {
    data class LaunchScoreboard(val id: Int? = null, val scoreboardType: ScoreboardType? = null) : UiEvent()
    data object Done : UiEvent()
    data class ScoreboardDetails(val id: Int? = null, val scoreboardType: ScoreboardType? = null) : UiEvent()
    data class TeamDetails(val id: Int? = null) : UiEvent()
    sealed class ShowSnackbar(
        open val message: Int,
        open val action: Int? = null
    ) : UiEvent() {
        class ShowGenericSnackbar(
            @StringRes override val message: Int,
            @StringRes override val action: Int? = null
        ) : ShowSnackbar(message, action)

        data class ShowQuantitySnackbar(
            @PluralsRes override val message: Int,
            val quantity: Int,
            @StringRes override val action: Int
        ) : ShowSnackbar(message, action)
    }
}
