package com.dgnt.quickScoreboardCreator.feature.history.presentation.historylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.HistoryDetails
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.SnackBar
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TimelineViewer
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.CategorizeHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryListUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel @Inject constructor(
    getHistoryListUseCase: GetHistoryListUseCase,
    private val insertHistoryListUseCase: InsertHistoryListUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val categorizeHistoryUseCase: CategorizeHistoryUseCase,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
    private val historyEntityList = getHistoryListUseCase()
    val state = historyEntityList.map {
        HistoryListState(
            categorizeHistoryUseCase(it)
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, HistoryListState())

    private var deletedHistoryList: MutableList<HistoryModel> = mutableListOf()

    fun onAction(action: HistoryListAction) {
        when (action) {
            HistoryListAction.ClearDeleteHistoryList -> onClearDeletedHistoryList()
            is HistoryListAction.Delete -> onDelete(action.id)
            is HistoryListAction.Edit -> onEdit(action.id)
            is HistoryListAction.Launch -> onLaunch(action.id)
            HistoryListAction.UndoDelete -> onUndoDelete()
        }
    }

    private fun onEdit(id: Int) = sendUiEvent(HistoryDetails(id))

    private fun onLaunch(id: Int) = sendUiEvent(TimelineViewer(id, 0))

    private fun onDelete(id: Int) = viewModelScope.launch {
        historyEntityList.first().find { entity ->
            entity.id == id
        }?.let {
            deletedHistoryList.add(it)
            deleteHistoryUseCase(it)
        }
        sendUiEvent(
            SnackBar.QuantitySnackBar(
                message = R.plurals.deletedHistoryMsg,
                quantity = deletedHistoryList.size,
                action = R.string.undo
            )
        )
    }

    private fun onUndoDelete() {
        deletedHistoryList.toList().takeUnless { it.isEmpty() }?.let { historyList ->
            viewModelScope.launch {
                insertHistoryListUseCase(historyList)
            }
            onClearDeletedHistoryList()
        }
    }

    private fun onClearDeletedHistoryList() = deletedHistoryList.clear()

}