package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.business.logic.TeamCategorizer
import com.dgnt.quickScoreboardCreator.domain.team.usecase.DeleteTeamListUseCase
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
    private val deleteTeamListUseCase: DeleteTeamListUseCase,
    private val teamCategorizer: TeamCategorizer,
) : ViewModel() {
    private val teamEntityList = getTeamListUseCase()
    val categorizedTeamList = teamEntityList.map {
        teamCategorizer(it)
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTeamList: List<TeamEntity>? = null

    fun onEvent(event: TeamListEvent) {
        when (event) {

            is TeamListEvent.OnAdd -> {
                sendUiEvent(UiEvent.TeamDetails())
            }

            is TeamListEvent.OnEdit -> {
                sendUiEvent(UiEvent.TeamDetails(event.team.id))
            }

            is TeamListEvent.OnDelete -> {
                viewModelScope.launch {
                    teamEntityList.first().find { entity ->
                        entity.id == event.id
                    }?.let { teamEntityToDelete ->
                        //TODO delete one thing instead of a list
                        val list = listOf(teamEntityToDelete)
                        deletedTeamList = list
                        deleteTeamListUseCase(list)
                    }
                    sendUiEvent(
                        UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                            message = R.plurals.deletedTeamMsg,
                            quantity = 1,
                            action = R.string.undo
                        )
                    )
                }
            }

            is TeamListEvent.OnUndoDelete -> {
                deletedTeamList?.let { teamList ->
                    viewModelScope.launch {
                        insertTeamListUseCase(teamList)
                    }
                }
            }

        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}