package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.domain.team.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.InsertTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamListViewModel @Inject constructor(
    getTeamListUseCase: GetTeamListUseCase,
    private val insertTeamListUseCase: InsertTeamListUseCase,
    private val deleteTeamUseCase: DeleteTeamUseCase,
    private val teamCategorizer: TeamCategorizer,
    uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
    private val teamEntityList = getTeamListUseCase()
    val categorizedTeamList = teamEntityList.map {
        teamCategorizer(it)
    }

    private var deletedTeamList: MutableList<TeamEntity> = mutableListOf()

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