package com.dgnt.quickScoreboardCreator.ui.scoreboard.intervaleditor

import android.content.res.Resources
import androidx.compose.runtime.getValue
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
    private var intervalIndex = 0

    var timeData by mutableStateOf(TimeData(0, 0, 0))

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Long>(Arguments.VALUE)?.let {
            currentTimeValue = it
            timeData = timeTransformer.toTimeData(it)
        }
        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            intervalIndex = it
        }
        savedStateHandle.get<Int>(Arguments.ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: savedStateHandle.get<ScoreboardType>(Arguments.TYPE)?.let {
            initWithScoreboardType(it)
        }
        viewModelScope.launch {
            snapshotFlow { timeData }
                .collect {
                    currentTimeValue = timeTransformer.fromTimeData(timeData)
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
        scoreboardType.rawRes?.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let {

            it.intervalList[intervalIndex].intervalData.let { data ->

            }
        }
    }

    fun onEvent(event: IntervalEditorEvent) {

        when (event) {
            IntervalEditorEvent.OnDismiss ->
                sendUiEvent(UiEvent.Done)

            IntervalEditorEvent.OnConfirm -> {

                sendUiEvent(UiEvent.Done)
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