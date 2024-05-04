package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.InsertTeamListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val insertTeamListUseCase: InsertTeamListUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var team by mutableStateOf<TeamEntity?>(null)
        private set

    var title by mutableStateOf("")

    var description by mutableStateOf("")

    var teamIcon by mutableStateOf(TeamIcon.ALIEN)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        }
        viewModelScope.launch {
            snapshotFlow { title }
                .collect {
                    sendUiEvent(UiEvent.Validation(validate()))
                }
        }
    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getTeamUseCase(id)?.let {
                title = it.title
                description = it.description
                teamIcon = it.teamIcon
                team = it
            }
        }
    }

    fun onEvent(event: TeamDetailsEvent) {
        when (event) {
            is TeamDetailsEvent.OnDone -> {
                viewModelScope.launch {
                    if (!validate())
                        return@launch

                    insertTeamListUseCase(
                        listOf(
                            TeamEntity(
                                id = team?.id,
                                title = title,
                                description = description,
                                teamIcon = teamIcon
                            )
                        )
                    )
                    sendUiEvent(UiEvent.Done)
                }
            }
        }
    }

    private fun validate() = title.isNotBlank()

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}