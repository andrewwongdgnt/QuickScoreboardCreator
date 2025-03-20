package com.dgnt.quickScoreboardCreator.feature.history.presentation.timelineviewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.TreeSet
import javax.inject.Inject

@HiltViewModel
class TimelineViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {


    private var intervalIndex = 0

    private var historicalScoreboard: HistoricalScoreboard? = null

    private val _state = MutableStateFlow(TimelineViewerState())
    val state: StateFlow<TimelineViewerState> = _state.asStateFlow()

    init {

        savedStateHandle.get<Int>(NavArguments.INDEX)?.let {
            intervalIndex = it
        }
        savedStateHandle.get<Int>(NavArguments.ID)?.let {
            initWithId(it)
        }
    }

    private fun initWithId(id: Int) = viewModelScope.launch {
        getHistoryUseCase(id)?.let {
            historicalScoreboard = it.historicalScoreboard
            historicalScoreboard
        }?.let {
            setTimeline(intervalIndex, it)
        }
    }

    fun onAction(action: TimelineViewerAction) {
        when (action) {
            is TimelineViewerAction.Dismiss -> onDismiss()
            is TimelineViewerAction.NewInterval -> onNewInterval(action.next)
        }
    }

    private fun onDismiss() = sendUiEvent(Done)

    private fun onNewInterval(next: Boolean) {
        historicalScoreboard?.let {
            val sortedSet = TreeSet(it.historicalIntervalMap.keys.toSortedSet())
            if (sortedSet.size > 1) {
                if (next) {
                    if (intervalIndex == sortedSet.last())
                        sortedSet.first()
                    else
                        sortedSet.tailSet(intervalIndex, false).first()
                } else {
                    if (intervalIndex == sortedSet.first())
                        sortedSet.last()
                    else
                        sortedSet.headSet(intervalIndex, false).last()
                }.let { intervalIndex ->
                    setTimeline(intervalIndex, it)
                }

            }
        }
    }

    private fun setTimeline(intervalIndex: Int, historicalScoreboard: HistoricalScoreboard) {
        this.intervalIndex = intervalIndex
        _state.update { state ->
            state.copy(
                historicalIntervalState = historicalScoreboard.historicalIntervalMap[intervalIndex]?.let {
                    HistoricalIntervalState.Loaded(it)
                } ?: HistoricalIntervalState.None
            )
        }
    }
}