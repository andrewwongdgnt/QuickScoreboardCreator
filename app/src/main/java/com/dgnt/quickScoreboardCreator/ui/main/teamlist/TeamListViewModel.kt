package com.dgnt.quickScoreboardCreator.ui.main.teamlist

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
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
    private val resources: Resources,
    getTeamListUseCase: GetTeamListUseCase,
    private val insertTeamListUseCase: InsertTeamListUseCase,
    private val deleteTeamListUseCase: DeleteTeamListUseCase,
) : ViewModel() {
    private val teamEntityList = getTeamListUseCase()
    val teamList = teamEntityList.map {
        it.map { e ->
            TeamItemData(
                e.id, e.title, e.description
            )
        }
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
                    teamEntityList.first().filter { entity ->
                        entity.id in event.teamList.map { it.id }
                    }.let { teamEntityList ->
                        deletedTeamList = teamEntityList
                        deleteTeamListUseCase(teamEntityList)
                    }
                    sendUiEvent(
                        UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                            message = R.plurals.deletedTeamMsg,
                            quantity = event.teamList.size,
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