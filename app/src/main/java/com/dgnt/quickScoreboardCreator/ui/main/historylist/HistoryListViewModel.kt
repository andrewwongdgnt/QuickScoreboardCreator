package com.dgnt.quickScoreboardCreator.ui.main.historylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.HistoryCategorizer
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryListUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryListUseCase
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel @Inject constructor(
    getHistoryListUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryListUseCase,
    private val insertHistoryListUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryListUseCase,
    private val deleteHistoryUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase,
    private val historyCategorizer: com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.HistoryCategorizer,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
    private val historyEntityList = getHistoryListUseCase()
    val categorizedHistoryList = historyEntityList.map {
        historyCategorizer(it)
    }

    private var deletedHistoryList: MutableList<com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel> = mutableListOf()

    fun onEdit(id: Int) = sendUiEvent(UiEvent.HistoryDetails(id))

    fun onLaunch(id: Int) = sendUiEvent(UiEvent.TimelineViewer(id, 0))

    fun onDelete(id: Int) = viewModelScope.launch {
        historyEntityList.first().find { entity ->
            entity.id == id
        }?.let {
            deletedHistoryList.add(it)
            deleteHistoryUseCase(it)
        }
        sendUiEvent(
            UiEvent.SnackBar.QuantitySnackBar(
                message = R.plurals.deletedHistoryMsg,
                quantity = deletedHistoryList.size,
                action = R.string.undo
            )
        )
    }

    fun onUndoDelete() {
        deletedHistoryList.toList().takeUnless { it.isEmpty() }?.let { historyList ->
            viewModelScope.launch {
                insertHistoryListUseCase(historyList)
            }
            onClearDeletedHistoryList()
        }
    }

    fun onClearDeletedHistoryList() = deletedHistoryList.clear()

}