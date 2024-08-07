package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.domain.common.mapper.Mapper
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.history.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.TreeSet
import javax.inject.Inject

@HiltViewModel
class TimelineViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val historyScoreboardDomainMapper: Mapper<HistoricalScoreboardData, HistoricalScoreboard>,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {


    private var intervalIndex = 0

    private var _icon = MutableStateFlow<ScoreboardIcon?>(null)
    val icon = _icon.asStateFlow()

    private var historicalScoreboard: HistoricalScoreboard? = null

    private var _historicalInterval = MutableStateFlow<HistoricalInterval?>(null)
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
            historicalScoreboard = historyScoreboardDomainMapper.map(it.historicalScoreboard)
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