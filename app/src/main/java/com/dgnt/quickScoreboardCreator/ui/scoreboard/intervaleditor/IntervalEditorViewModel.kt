package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntervalEditorViewModel @Inject constructor(
    private val resources: Resources,
    private val getScoreboardUseCase: GetScoreboardUseCase,
    private val timeTransformer: TimeTransformer,
    private val scoreboardLoader: ScoreboardLoader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentTimeValue = 0L
    var interval by mutableIntStateOf(1)
        private set

    var labelInfo by mutableStateOf(Pair<String?, Int?>(null, null))
        private set

    var timeData by mutableStateOf(TimeData(0, 0, 0))
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Long>(Arguments.VALUE)?.let {
            currentTimeValue = it
            timeData = timeTransformer.toTimeData(it)
        }
        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            interval = it + 1
        }
        savedStateHandle.get<Int>(Arguments.ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: savedStateHandle.get<ScoreboardType>(Arguments.TYPE)?.let {
            initWithScoreboardType(it)
        }
        viewModelScope.launch {
            snapshotFlow { timeData }
                .collect {
                    currentTimeValue = timeTransformer.fromTimeData(it)
                }
        }
    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getScoreboardUseCase(id)?.let {

            }
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {
        labelInfo = null to scoreboardType.intervalLabelRes
        scoreboardType.rawRes?.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let {


        }
    }

    fun onEvent(event: IntervalEditorEvent) {

        when (event) {
            IntervalEditorEvent.OnDismiss ->
                sendUiEvent(UiEvent.Done)

            IntervalEditorEvent.OnConfirm -> {

                sendUiEvent(UiEvent.IntervalUpdated(currentTimeValue, interval - 1))
            }

            is IntervalEditorEvent.OnMinuteChange -> {
                getIntValue(event.value)?.let { min ->
                    timeData = TimeData(min, timeData.second, timeData.centiSecond)
                }
            }

            is IntervalEditorEvent.OnSecondChange -> {
                getIntValue(event.value)?.let { second ->
                    timeData = TimeData(timeData.minute, second, timeData.centiSecond)
                }
            }

            is IntervalEditorEvent.OnIntervalChange -> {
                getIntValue(event.value)?.let { interval ->
                    this.interval = interval.coerceAtLeast(1)
                }
            }
        }

    }

    private fun getIntValue(value: String) = if (value.isEmpty())
        0
    else if (value.isDigitsOnly())
        value.toInt()
    else
        null

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}