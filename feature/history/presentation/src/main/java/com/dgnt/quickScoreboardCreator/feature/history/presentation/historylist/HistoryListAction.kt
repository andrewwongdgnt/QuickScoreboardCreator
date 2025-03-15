package com.dgnt.quickScoreboardCreator.feature.history.presentation.historylist

sealed interface HistoryListAction {
    data class Edit(val id: Int) : HistoryListAction
    data class Launch(val id: Int) : HistoryListAction
    data class Delete(val id: Int) : HistoryListAction
    data object UndoDelete : HistoryListAction
    data object ClearDeleteHistoryList : HistoryListAction
}