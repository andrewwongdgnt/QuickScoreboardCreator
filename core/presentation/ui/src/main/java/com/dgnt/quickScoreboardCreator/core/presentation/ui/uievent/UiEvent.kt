package com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent

import androidx.annotation.PluralsRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes

interface UiEvent

//TODO move some of these to their own module
data class HistoryDetails(val id: Int = -1) : UiEvent
data class TeamPicker(val index: Int) : UiEvent
data class TeamUpdated(val index: Int, val id: Int) : UiEvent
data class IntervalUpdated(val timeValue: Long, val index: Int) : UiEvent
data class TimelineViewer(val id: Int = -1, val index: Int) : UiEvent

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

