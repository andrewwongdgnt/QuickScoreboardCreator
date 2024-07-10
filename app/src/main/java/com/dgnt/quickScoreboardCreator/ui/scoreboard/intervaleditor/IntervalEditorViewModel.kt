package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.common.Label
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.time.TimeData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

@HiltViewModel
class IntervalEditorViewModel @Inject constructor(
    private val resources: Resources,
    private val getScoreboardUseCase: GetScoreboardUseCase,
    private val timeTransformer: TimeTransformer,
    private val scoreboardLoader: ScoreboardLoader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _minuteString = MutableStateFlow("")
    val minuteString = _minuteString.asStateFlow()

    private val _secondString = MutableStateFlow("")
    val secondString = _secondString.asStateFlow()

    private var centiSecond = 0

    private val initialTimeValue
        get() = intervalList.getOrNull(intervalValue - 1)?.intervalData?.initial
    private val isTimeIncreasing
        get() = intervalList.getOrNull(intervalValue - 1)?.intervalData?.increasing

    private var intervalList = listOf<IntervalConfig>()
    private var currentTimeValue = 0L
        set(value) {
            field = value
            validate()
        }
    private val _intervalString = MutableStateFlow("")
    val intervalString = _intervalString.asStateFlow()

    private var maxInterval = 1
    private var intervalValue = 1
        set(value) {
            field = value
            validate()
        }

    private val _label = MutableStateFlow<Label>(Label.CustomLabel(""))
    val label = _label.asStateFlow()

    private val _errors = MutableStateFlow(emptySet<IntervalEditorErrorType>())
    val errors = _errors.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<ScoreboardIdentifier>(Arguments.SCOREBOARD_IDENTIFIER)?.let { sId ->
            when (sId) {
                is ScoreboardIdentifier.CustomScoreboard -> initWithId(sId.id)
                is ScoreboardIdentifier.DefaultScoreboard -> initWithScoreboardType(sId.scoreboardType)
            }
        }
        savedStateHandle.get<Long>(Arguments.VALUE)?.let {
            currentTimeValue = it
            timeTransformer.toTimeData(it).let { td ->
                _minuteString.value = td.minute.toString()
                _secondString.value = td.second.toString()
                centiSecond = td.centiSecond
            }
        }
        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            _intervalString.value = (it + 1).toString()
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
        _label.value = Label.ResourceLabel(scoreboardType.intervalLabelRes)
        scoreboardType.rawRes.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let {
            intervalList = it.intervalList
            maxInterval = it.intervalList.size
        }
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onConfirm(){
        val initialTimeValue = initialTimeValue
        val isTimeIncreasing = isTimeIncreasing
        if (initialTimeValue == null || isTimeIncreasing == null) {
            sendUiEvent(UiEvent.Done)
        } else {
            val newCurrentTimeValue = if (!isTimeIncreasing)
                currentTimeValue.coerceIn(0, initialTimeValue)
            else
                currentTimeValue.coerceAtLeast(0)
            sendUiEvent(UiEvent.IntervalUpdated(newCurrentTimeValue, (intervalValue - 1).coerceIn(0, maxInterval - 1)))
        }
    }

    fun onMinuteChange(value: String){
        getFilteredValue(value)?.let { min ->
            _minuteString.value = min
            centiSecond = 0
            currentTimeValue = TimeData(
                (min.toIntOrNull() ?: 0).coerceAtLeast(0),
                (secondString.value.toIntOrNull() ?: 0).coerceAtLeast(0),
                centiSecond
            ).let {
                timeTransformer.fromTimeData(it)
            }
        }
    }

    fun onSecondChange(value: String){
        getFilteredValue(value)?.let { second ->
            _secondString.value = second
            centiSecond = 0
            currentTimeValue = TimeData(
                (minuteString.value.toIntOrNull() ?: 0).coerceAtLeast(0),
                (second.toIntOrNull() ?: 0).coerceAtLeast(0),
                centiSecond
            ).let {
                timeTransformer.fromTimeData(it)
            }
        }
    }

    fun onIntervalChange(value: String) {
        getFilteredValue(value)?.let { interval ->
            _intervalString.value = interval
            intervalValue = interval.toIntOrNull() ?: 0
        }
    }

    private fun getFilteredValue(value: String) = if (value.isEmpty())
        ""
    else if (StringUtils.isNumeric(value))
        value
    else
        null

    private fun validate() {
        val errors = mutableSetOf<IntervalEditorErrorType>()
        if (minuteString.value.isEmpty() || secondString.value.isEmpty())
            errors.add(IntervalEditorErrorType.TimeErrorType.EmptyTime)
        if (intervalString.value.isEmpty())
            errors.add(IntervalEditorErrorType.IntervalErrorType.EmptyInterval)

        val initialTimeValue = initialTimeValue
        val isTimeIncreasing = isTimeIncreasing
        if (initialTimeValue != null && isTimeIncreasing != null && !isTimeIncreasing) {
            if (currentTimeValue > initialTimeValue)
                timeTransformer.toTimeData(initialTimeValue).let {
                    errors.add(IntervalEditorErrorType.TimeErrorType.Time(it.minute, it.second))
                }
            else if (currentTimeValue <= 0 && !errors.contains(IntervalEditorErrorType.TimeErrorType.EmptyTime))
                errors.add(IntervalEditorErrorType.TimeErrorType.ZeroTime)
        }

        if (!errors.contains(IntervalEditorErrorType.IntervalErrorType.EmptyInterval) && (intervalValue <= 0 || intervalValue > maxInterval)) {
            errors.add(IntervalEditorErrorType.IntervalErrorType.Interval(maxInterval))
        }

        _errors.value = errors
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}