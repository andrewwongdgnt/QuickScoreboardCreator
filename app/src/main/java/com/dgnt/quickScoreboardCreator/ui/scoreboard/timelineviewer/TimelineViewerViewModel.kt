package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIcon
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.TreeSet
import javax.inject.Inject

@HiltViewModel
class TimelineViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHistoryUseCase: com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {


    private var intervalIndex = 0

    private var _icon = MutableStateFlow<SportIcon?>(null)
    val icon = _icon.asStateFlow()

    private var historicalScoreboard: HistoricalScoreboard? = null

    private var _historicalInterval = MutableStateFlow<com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalInterval?>(null)
    val historicalInterval = _historicalInterval.asStateFlow()


    init {

        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            intervalIndex = it
        }
        savedStateHandle.get<Int>(Arguments.ID)?.let {
            initWithId(it)
        }
    }

    private fun initWithId(id: Int) = viewModelScope.launch {
        getHistoryUseCase(id)?.let {
            _icon.value = it.icon
            historicalScoreboard = it.historicalScoreboard
            historicalScoreboard
        }?.let {
            setTimeline(intervalIndex, it)
        }
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onNewInterval(next: Boolean) {
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
        _historicalInterval.value = historicalScoreboard.historicalIntervalMap[intervalIndex]
    }
}