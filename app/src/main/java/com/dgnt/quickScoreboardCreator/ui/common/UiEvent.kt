package com.dgnt.quickScoreboardCreator.ui.common

import androidx.annotation.PluralsRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon

sealed interface UiEvent {
    data class LaunchScoreboard(val scoreboardIdentifier: ScoreboardIdentifier) : UiEvent
    data class ScoreboardDetails(val scoreboardIdentifier: ScoreboardIdentifier? = null) : UiEvent
    data class TeamDetails(val id: Int = -1) : UiEvent
    data class TeamPicker(val index: Int) : UiEvent
    data class TeamUpdated(val index: Int, val id: Int) : UiEvent
    data class IntervalEditor(val currentTimeValue: Long, val index: Int, val scoreboardIdentifier: ScoreboardIdentifier) : UiEvent
    data class IntervalUpdated(val timeValue: Long, val index: Int) : UiEvent
    data class TimelineViewer(val historicalScoreboard: HistoricalScoreboard, val index: Int, val title:String, val icon: ScoreboardIcon) : UiEvent
    data class TimelineSaver(val historicalScoreboard: HistoricalScoreboard, val title:String, val icon: ScoreboardIcon) : UiEvent

    data object Done : UiEvent
    sealed class SnackBar(
        open val message: Int,
        open val action: Int? = null
    ) : UiEvent {
        class GenericSnackBar(
            @StringRes override val message: Int,
            @StringRes override val action: Int? = null
        ) : SnackBar(message, action)

        data class QuantitySnackBar(
            @PluralsRes override val message: Int,
            val quantity: Int,
            @StringRes override val action: Int
        ) : SnackBar(message, action)
    }

    data class PlaySound(@RawRes val soundRes: Int) : UiEvent
}
