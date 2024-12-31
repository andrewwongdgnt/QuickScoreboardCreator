package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamListUseCase
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamListViewModel @Inject constructor(
    getTeamListUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamListUseCase,
    private val insertTeamListUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamListUseCase,
    private val deleteTeamUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase,
    private val teamCategorizer: com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.TeamCategorizer,
    uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
    private val teamEntityList = getTeamListUseCase()
    val categorizedTeamList = teamEntityList.map {
        teamCategorizer(it)
    }

    private var deletedTeamList: MutableList<com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel> = mutableListOf()

    fun onAdd() = sendUiEvent(UiEvent.TeamDetails())

    fun onEdit(id: Int) = sendUiEvent(UiEvent.TeamDetails(id))

    fun onDelete(id: Int) = viewModelScope.launch {
        teamEntityList.first().find { entity ->
            entity.id == id
        }?.let {
            deletedTeamList.add(it)
            deleteTeamUseCase(it)
        }
        sendUiEvent(
            UiEvent.SnackBar.QuantitySnackBar(
                message = R.plurals.deletedTeamMsg,
                quantity = deletedTeamList.size,
                action = R.string.undo
            )
        )
    }

    fun onUndoDelete() {
        deletedTeamList.toList().takeUnless { it.isEmpty() }?.let { teamList ->
            viewModelScope.launch {
                insertTeamListUseCase(teamList)
            }
            onClearDeletedTeamList()
        }
    }

    fun onClearDeletedTeamList() = deletedTeamList.clear()

}