package com.dgnt.quickScoreboardCreator.feature.history.presentation.historydetails

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon

data class HistoryDetailsState(
    val title: String = "",
    val description: String = "",
    val iconState: HistoryIconState = HistoryIconState.Initial,
) {
    val valid
        get() = title.isNotBlank()
}

sealed interface HistoryIconState {
    data object Initial : HistoryIconState
    sealed class Picked(val sportIcon: SportIcon) : HistoryIconState {
        class Changing(sportIcon: SportIcon) : Picked(sportIcon)
        class Displaying(sportIcon: SportIcon) : Picked(sportIcon)
    }
}