package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.domain.team.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.InsertTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamListViewModel @Inject constructor(
    getTeamListUseCase: GetTeamListUseCase,
    private val insertTeamListUseCase: InsertTeamListUseCase,
    private val deleteTeamUseCase: DeleteTeamUseCase,
    private val teamCategorizer: TeamCategorizer,
) : ViewModel() {
    private val teamEntityList = getTeamListUseCase()
    val categorizedTeamList = teamEntityList.map {
        teamCategorizer(it)
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTeamList: MutableList<TeamEntity> = mutableListOf()

    fun onEvent(event: TeamListEvent) {
        when (event) {

            TeamListEvent.OnAdd -> {
                sendUiEvent(UiEvent.TeamDetails())
            }

            is TeamListEvent.OnEdit -> {
                sendUiEvent(UiEvent.TeamDetails(event.id))
            }

            is TeamListEvent.OnDelete -> {
                viewModelScope.launch {
                    teamEntityList.first().find { entity ->
                        entity.id == event.id
                    }?.let {
                        deletedTeamList.add(it)
                        deleteTeamUseCase(it)
                    }
                    sendUiEvent(
                        UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                            message = R.plurals.deletedTeamMsg,
                            quantity = deletedTeamList.size,
                            action = R.string.undo
                        )
                    )
                }
            }

            TeamListEvent.OnUndoDelete -> {
                val teamList = deletedTeamList.toList()
                viewModelScope.launch {
                    insertTeamListUseCase(teamList)
                }
                onEvent(TeamListEvent.OnClearDeletedTeamList)
            }

            TeamListEvent.OnClearDeletedTeamList -> {
                deletedTeamList.clear()
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}