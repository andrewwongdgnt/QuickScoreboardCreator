package com.dgnt.quickScoreboardCreator.ui.main.historylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.history.business.logic.HistoryCategorizer
import com.dgnt.quickScoreboardCreator.domain.history.usecase.DeleteHistoryUseCase
import com.dgnt.quickScoreboardCreator.domain.history.usecase.GetHistoryListUseCase
import com.dgnt.quickScoreboardCreator.domain.history.usecase.InsertHistoryListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel @Inject constructor(
    getHistoryListUseCase: GetHistoryListUseCase,
    private val insertHistoryListUseCase: InsertHistoryListUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val historyCategorizer: HistoryCategorizer,
) : ViewModel() {
    private val historyEntityList = getHistoryListUseCase()
    val categorizedHistoryList = historyEntityList.map {
        historyCategorizer(it)
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedHistoryList: MutableList<HistoryEntity> = mutableListOf()

    fun onEdit(id: Int) = sendUiEvent(UiEvent.HistoryDetails(id))
    fun onLaunch(id: Int) {

        //TODO launch timeline viewer
    }

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

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}