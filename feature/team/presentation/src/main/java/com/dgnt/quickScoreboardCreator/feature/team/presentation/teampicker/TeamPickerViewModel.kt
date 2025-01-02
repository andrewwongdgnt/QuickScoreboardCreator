package com.dgnt.quickScoreboardCreator.feature.team.presentation.teampicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.INDEX
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TeamUpdated
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.CategorizeTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TeamPickerViewModel @Inject constructor(
    categorizeTeamUseCase: CategorizeTeamUseCase,
    getTeamListUseCase: GetTeamListUseCase,
    savedStateHandle: SavedStateHandle,
    uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
    private val teamEntityList = getTeamListUseCase()
    val categorizedTeamList = teamEntityList.map {
        categorizeTeamUseCase(it)
    }

    private var scoreIndex = 0

    init {
        savedStateHandle.get<Int>(INDEX)?.let {
            scoreIndex = it
        }
    }

    fun onDismiss() = sendUiEvent(Done)

    fun onTeamPicked(id: Int) = sendUiEvent(TeamUpdated(scoreIndex, id))
}