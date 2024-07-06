package com.dgnt.quickScoreboardCreator.ui.common

import androidx.annotation.PluralsRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType

sealed class UiEvent {
    data class LaunchScoreboard(val id: Int = -1, val type: ScoreboardType = ScoreboardType.NONE) : UiEvent()
    data object Done : UiEvent()
    data class ScoreboardDetails(val id: Int = -1, val type: ScoreboardType = ScoreboardType.NONE) : UiEvent()
    data class TeamDetails(val id: Int = -1) : UiEvent()
    data class TeamPicker(val index: Int) : UiEvent()
    data class TeamUpdated(val index: Int, val id: Int) : UiEvent()
    data class IntervalEditor(val currentTimeValue: Long, val index: Int, val id: Int = -1, val type: ScoreboardType = ScoreboardType.NONE) : UiEvent()
    data class IntervalUpdated(val timeValue: Long, val index: Int) : UiEvent()
    data class TimelineViewer(val historicalScoreboard: HistoricalScoreboard, val index: Int, val id: Int = -1, val type: ScoreboardType = ScoreboardType.NONE) : UiEvent()
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

    data class PlaySound(@RawRes val soundRes: Int) : UiEvent()
}
