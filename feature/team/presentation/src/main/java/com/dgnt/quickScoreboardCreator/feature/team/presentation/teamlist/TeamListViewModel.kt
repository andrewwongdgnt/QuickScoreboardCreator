package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.SnackBar
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.CategorizeTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamListUseCase
import com.dgnt.quickScoreboardCreator.feature.team.presentation.uievent.TeamDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamListViewModel @Inject constructor(
    getTeamListUseCase: GetTeamListUseCase,
    private val insertTeamListUseCase: InsertTeamListUseCase,
    private val deleteTeamUseCase: DeleteTeamUseCase,
    private val categorizeTeamUseCase: CategorizeTeamUseCase,
    uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
    private val teamEntityList = getTeamListUseCase()
    val state = teamEntityList.map {
        TeamListState(
            categorizedTeamList = categorizeTeamUseCase(it)
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, TeamListState())

    private var deletedTeamList: MutableList<TeamModel> = mutableListOf()

    fun onAction(action: TeamListAction){
        when(action){
            TeamListAction.Add -> onAdd()
            is TeamListAction.Edit -> onEdit(action.id)
            is TeamListAction.Delete -> onDelete(action.id)
            TeamListAction.UndoDelete -> onUndoDelete()
            TeamListAction.ClearDeletedTeamList -> onClearDeletedTeamList()
        }
    }

    private fun onAdd() = sendUiEvent(TeamDetails())

    private fun onEdit(id: Int) = sendUiEvent(TeamDetails(id))

    private fun onDelete(id: Int) = viewModelScope.launch {
        teamEntityList.first().find { entity ->
            entity.id == id
        }?.let {
            deletedTeamList.add(it)
            deleteTeamUseCase(it)
        }
        sendUiEvent(
            SnackBar.QuantitySnackBar(
                message = R.plurals.deletedTeamMsg,
                quantity = deletedTeamList.size,
                action = R.string.undo
            )
        )
    }

    private fun onUndoDelete() {
        deletedTeamList.toList().takeUnless { it.isEmpty() }?.let { teamList ->
            viewModelScope.launch {
                insertTeamListUseCase(teamList)
            }
            onClearDeletedTeamList()
        }
    }

    private fun onClearDeletedTeamList() = deletedTeamList.clear()

}