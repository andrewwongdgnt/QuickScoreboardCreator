package com.dgnt.quickScoreboardCreator.ui.common

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType

sealed class UiEvent {
    data class LaunchScoreboard(val id: Int? = null, val scoreboardType: ScoreboardType? = null) : UiEvent()
    data object Done : UiEvent()
    data class ScoreboardDetails(val id: Int? = null, val scoreboardType: ScoreboardType? = null) : UiEvent()
    data class TeamDetails(val id: Int? = null) : UiEvent()
    data class TeamPicker(val scoreIndex: Int) : UiEvent()
    data class TeamPicked(val scoreIndex: Int, val teamId: Int) : UiEvent()
    data class IntervalEditor(val currentTimeValue: Long, val intervalIndex: Int, val id: Int? = null, val scoreboardType: ScoreboardType? = null) : UiEvent()
    data class IntervalEdited(val timeValue: Long) : UiEvent()
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
