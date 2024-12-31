package com.dgnt.quickScoreboardCreator.ui.scoreboard.teampicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamListUseCase

import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TeamPickerViewModel @Inject constructor(
    teamCategorizer: com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.TeamCategorizer,
    getTeamListUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamListUseCase,
    savedStateHandle: SavedStateHandle,
    uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
    private val teamEntityList = getTeamListUseCase()
    val categorizedTeamList = teamEntityList.map {
        teamCategorizer(it)
    }

    private var scoreIndex = 0

    init {
        savedStateHandle.get<Int>(Arguments.INDEX)?.let {
            scoreIndex = it
        }
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onTeamPicked(id: Int) = sendUiEvent(UiEvent.TeamUpdated(scoreIndex, id))
}