package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val insertTeamListUseCase: InsertTeamListUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var teamId: Int? = null

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _icon = MutableStateFlow<TeamIcon?>(null)
    val icon: StateFlow<TeamIcon?> = _icon.asStateFlow()

    private val _iconChanging = MutableStateFlow(false)
    val iconChanging: StateFlow<Boolean> = _iconChanging.asStateFlow()

    val valid: StateFlow<Boolean> = title.map {
        it.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: run {
            _icon.value = TeamIcon.entries.toTypedArray().let {
                it[Random.nextInt(it.size)]
            }
        }
    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getTeamUseCase(id)?.let {
                _title.value = it.title
                _description.value = it.description
                _icon.value = it.icon
                teamId = it.id
            }
        }
    }

    fun onConfirm() {
        if (valid.value) {
            viewModelScope.launch {
                insertTeamListUseCase(
                    listOf(
                        TeamEntity(
                            id = teamId,
                            title = title.value,
                            description = description.value,
                            icon = icon.value!!
                        )
                    )
                )
            }
        }
        sendUiEvent(UiEvent.Done)
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onIconEdit() {
        _iconChanging.value = true
    }

    fun onIconChange(icon: TeamIcon) {
        _icon.value = icon
        _iconChanging.value = false
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}