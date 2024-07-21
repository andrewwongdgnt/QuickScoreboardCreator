package com.dgnt.quickScoreboardCreator.ui.scoreboard.timelinesaver

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.common.mapper.Mapper
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.domain.history.usecase.InsertHistoryListUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class TimelineSaverViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mapper: Mapper<HistoricalScoreboard, HistoricalScoreboardData>,
    private val insertHistoryListUseCase: InsertHistoryListUseCase
) : ViewModel() {

    private var _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private var _icon = MutableStateFlow<ScoreboardIcon?>(null)
    val icon = _icon.asStateFlow()

    private var historicalScoreboard: HistoricalScoreboard? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val lastLookedAt = DateTime.now()

    init {

        savedStateHandle.get<String>(Arguments.TITLE)?.let {
            _title.value = it
        }
        savedStateHandle.get<ScoreboardIcon>(Arguments.ICON)?.let {
            _icon.value = it
        }

        savedStateHandle.get<HistoricalScoreboard>(Arguments.HISTORICAL_SCOREBOARD)?.let {
            historicalScoreboard = it
        }
    }

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onSave() = viewModelScope.launch {
        historicalScoreboard?.let { historicalScoreboard ->
            insertHistoryListUseCase(
                listOf(
                    HistoryEntity(
                        title = title.value,
                        icon = ScoreboardIcon.SOCCER,
                        lastLookedAt = lastLookedAt,
                        historicalScoreboard = mapper.map(historicalScoreboard)
                    )
                )
            )
        }
        sendUiEvent(UiEvent.Done)
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}