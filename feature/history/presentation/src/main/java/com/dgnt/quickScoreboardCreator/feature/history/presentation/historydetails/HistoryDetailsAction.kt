package com.dgnt.quickScoreboardCreator.feature.history.presentation.historydetails

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon

sealed interface HistoryDetailsAction {
    data object Confirm : HistoryDetailsAction
    data object Dismiss : HistoryDetailsAction
    data object Delete : HistoryDetailsAction
    data class TitleChange(val title: String) : HistoryDetailsAction
    data class DescriptionChange(val description: String) : HistoryDetailsAction
    data class IconEdit(val changing: Boolean) : HistoryDetailsAction
    data class IconChange(val icon: SportIcon) : HistoryDetailsAction

}