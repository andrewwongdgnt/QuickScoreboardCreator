package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.intervaleditor

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.IntervalUpdated
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.GetSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.TimeConversionUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.intervalLabelRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.rawRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

@HiltViewModel
class IntervalEditorViewModel @Inject constructor(
    private val resources: Resources,
    private val getSportUseCase: GetSportUseCase,
    private val timeConversionUseCase: TimeConversionUseCase,
    savedStateHandle: SavedStateHandle,
    uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {

    private val _minuteString = MutableStateFlow("")
    val minuteString = _minuteString.asStateFlow()

    private val _secondString = MutableStateFlow("")
    val secondString = _secondString.asStateFlow()

    private var centiSecond = 0

    private val initialTimeValue
        get() = intervalList.getOrNull(intervalValue - 1)?.second?.initial
    private val isTimeIncreasing
        get() = intervalList.getOrNull(intervalValue - 1)?.second?.increasing

    private var intervalList = listOf<Pair<ScoreInfo, IntervalData>>()
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

    private val _label = MutableStateFlow<Label>(Label.Custom(""))
    val label = _label.asStateFlow()

    private val _errors = MutableStateFlow(emptySet<IntervalEditorErrorType>())
    val errors = _errors.asStateFlow()

    init {
        savedStateHandle.get<SportIdentifier>(NavArguments.SPORT_IDENTIFIER)?.let { sId ->
            when (sId) {
                is SportIdentifier.Custom -> initWithId(sId.id)
                is SportIdentifier.Default -> initWithSportType(sId.sportType)
            }
        }
        savedStateHandle.get<Long>(NavArguments.VALUE)?.let {
            currentTimeValue = it
            timeConversionUseCase.toTimeData(it).let { td ->
                _minuteString.value = td.minute.toString()
                _secondString.value = td.second.toString()
                centiSecond = td.centiSecond
            }
        }
        savedStateHandle.get<Int>(NavArguments.INDEX)?.let {
            _intervalString.value = (it + 1).toString()
            intervalValue = it + 1
        }
    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getSportUseCase(id)?.let {

            }
        }
    }

    private fun initWithSportType(sportType: SportType) {
        _label.value = Label.Resource(sportType.intervalLabelRes())
        getSportUseCase(resources.openRawResource(sportType.rawRes()))?.let {
            intervalList = it.intervalList
            maxInterval = it.intervalList.size
        }
    }

    fun onDismiss() = sendUiEvent(Done)

    fun onConfirm(){
        val initialTimeValue = initialTimeValue
        val isTimeIncreasing = isTimeIncreasing
        if (initialTimeValue == null || isTimeIncreasing == null) {
            sendUiEvent(Done)
        } else {
            val newCurrentTimeValue = if (!isTimeIncreasing)
                currentTimeValue.coerceIn(0, initialTimeValue)
            else
                currentTimeValue.coerceAtLeast(0)
            sendUiEvent(IntervalUpdated(newCurrentTimeValue, (intervalValue - 1).coerceIn(0, maxInterval - 1)))
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
                timeConversionUseCase.fromTimeData(it)
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
                timeConversionUseCase.fromTimeData(it)
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
            errors.add(IntervalEditorErrorType.Time.Empty)
        if (intervalString.value.isEmpty())
            errors.add(IntervalEditorErrorType.Interval.Empty)

        val initialTimeValue = initialTimeValue
        val isTimeIncreasing = isTimeIncreasing
        if (initialTimeValue != null && isTimeIncreasing != null && !isTimeIncreasing) {
            if (currentTimeValue > initialTimeValue)
                timeConversionUseCase.toTimeData(initialTimeValue).let {
                    errors.add(IntervalEditorErrorType.Time.Invalid(it.minute, it.second))
                }
            else if (currentTimeValue <= 0 && !errors.contains(IntervalEditorErrorType.Time.Empty))
                errors.add(IntervalEditorErrorType.Time.Zero)
        }

        if (!errors.contains(IntervalEditorErrorType.Interval.Empty) && (intervalValue <= 0 || intervalValue > maxInterval)) {
            errors.add(IntervalEditorErrorType.Interval.Invalid(maxInterval))
        }

        _errors.value = errors
    }
}