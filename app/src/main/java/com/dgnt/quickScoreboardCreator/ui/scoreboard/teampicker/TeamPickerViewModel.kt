package com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.domain.team.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamPickerViewModel @Inject constructor(
    private val teamCategorizer: TeamCategorizer,
    getTeamListUseCase: GetTeamListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val teamEntityList = getTeamListUseCase()
    val categorizedTeamList = teamEntityList.map {
        teamCategorizer(it)
    }

    private var scoreIndex = 0

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(Arguments.SCORE_INDEX)?.let {
            scoreIndex = it
        }
    }

    fun onEvent(event: TeamPickerEvent) {
        viewModelScope.launch {
            when (event) {
                is TeamPickerEvent.OnDone ->
                    sendUiEvent(UiEvent.Done)

                is TeamPickerEvent.OnTeamPicked ->
                    sendUiEvent(UiEvent.TeamPicked(scoreIndex, event.teamId))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}