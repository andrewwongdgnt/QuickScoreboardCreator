package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var minuteString by mutableStateOf("")
        private set
    var secondString by mutableStateOf("")
        private set

    private var centiSecond = 0

    private var initialTimeValue = 0L
    private var isTimeIncreasing = false
    private var currentTimeValue = 0L
        set(value) {
            if (value > initialTimeValue && !isTimeIncreasing) {
                timeTransformer.toTimeData(initialTimeValue).let {
                    errors = errors + IntervalEditorErrorType.Time(it.minute, it.second)
                }
            } else {
                errors.find { it is IntervalEditorErrorType.Time }?.let {
                    errors = errors - it
                }
            }
            field = value
        }
    var intervalString by mutableStateOf("")
        private set

    private var maxInterval = 1
    private var intervalValue = 1
        set(value) {
            if (value < 0 || value > maxInterval) {
                errors = errors + IntervalEditorErrorType.Interval(maxInterval)
            } else {
                errors.find { it is IntervalEditorErrorType.Interval }?.let {
                    errors = errors - it
                }
            }
            field = value
        }

    var labelInfo by mutableStateOf(Pair<String?, Int?>(null, null))
        private set

    var errors by mutableStateOf(emptySet<IntervalEditorErrorType>())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(Arguments.ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: savedStateHandle.get<ScoreboardType>(Arguments.TYPE)?.let {
            initWithScoreboardType(it)
        }
        savedStateHandle.get<Long>(Arguments.VALUE)?.let {
            currentTimeValue = it
            timeTransformer.toTimeData(it).let { td ->
                minuteString = td.minute.toString()
                secondString = td.second.toString()
                centiSecond = td.centiSecond
            }
        }
        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            intervalString = (it + 1).toString()
            intervalValue = it + 1
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

            initialTimeValue = it.intervalList[intervalValue - 1].intervalData.initial
            isTimeIncreasing = it.intervalList[intervalValue - 1].intervalData.increasing
            maxInterval = it.intervalList.size
        }
    }

    fun onEvent(event: IntervalEditorEvent) {

        when (event) {
            IntervalEditorEvent.OnDismiss ->
                sendUiEvent(UiEvent.Done)

            IntervalEditorEvent.OnConfirm -> {
                val newCurrentTimeValue = if (!isTimeIncreasing)
                    currentTimeValue.coerceIn(0, initialTimeValue)
                else
                    currentTimeValue.coerceAtLeast(0)
                sendUiEvent(UiEvent.IntervalUpdated(newCurrentTimeValue, (intervalValue - 1).coerceIn(0, maxInterval - 1)))
            }

            is IntervalEditorEvent.OnMinuteChange -> {
                getFilteredValue(event.value)?.let { min ->
                    minuteString = min
                    currentTimeValue = TimeData(
                        (min.toIntOrNull() ?: 0).coerceAtLeast(0),
                        (secondString.toIntOrNull() ?: 0).coerceAtLeast(0),
                        centiSecond
                    ).let {
                        timeTransformer.fromTimeData(it)
                    }
                }
            }

            is IntervalEditorEvent.OnSecondChange -> {
                getFilteredValue(event.value)?.let { second ->
                    secondString = second
                    currentTimeValue = TimeData(
                        (minuteString.toIntOrNull() ?: 0).coerceAtLeast(0),
                        (second.toIntOrNull() ?: 0).coerceAtLeast(0),
                        centiSecond
                    ).let {
                        timeTransformer.fromTimeData(it)
                    }
                }
            }

            is IntervalEditorEvent.OnIntervalChange -> {
                getFilteredValue(event.value)?.let { interval ->
                    intervalString = interval
                    intervalValue = (interval.toIntOrNull() ?: 1).coerceAtLeast(1)
                }
            }
        }

    }

    private fun getFilteredValue(value: String) = if (value.isEmpty())
        ""
    else if (value.isDigitsOnly())
        value
    else
        null

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}