package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelineviewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalInterval
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.TreeSet
import javax.inject.Inject

@HiltViewModel
class TimelineViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private var intervalIndex = 0

    private var defaultTitle = ""

    private var historicalScoreboard: HistoricalScoreboard? = null

    private var _historicalInterval = MutableStateFlow<HistoricalInterval?>(null)
    val historicalInterval = _historicalInterval.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val lastLookedAt = DateTime.now()

    init {

        savedStateHandle.get<String>(Arguments.TITLE)?.let {
            defaultTitle = it
        }
        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            intervalIndex = it
        }
        savedStateHandle.get<HistoricalScoreboard>(Arguments.HISTORICAL_SCOREBOARD)?.let {
            historicalScoreboard = it
            setTimeline(intervalIndex)
        }
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onSave()  {
        historicalScoreboard?.let { historicalScoreboard ->

            //TODO send ui event to open dialog to save the data
        }
    }

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
                    setTimeline(intervalIndex)
                }

            }
        }
    }

    private fun setTimeline(intervalIndex: Int) {
        this.intervalIndex = intervalIndex
        _historicalInterval.value = historicalScoreboard?.historicalIntervalMap?.get(intervalIndex)
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}