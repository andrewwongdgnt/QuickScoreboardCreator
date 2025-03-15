package com.dgnt.quickScoreboardCreator.feature.history.presentation.historylist

import com.dgnt.quickScoreboardCreator.feature.history.domain.model.CategorizedHistoryItemData

data class HistoryListState (
    val categorizedHistoryList: List<CategorizedHistoryItemData> = emptyList()
)