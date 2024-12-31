package com.dgnt.quickScoreboardCreator.ui.main.teamdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val insertTeamUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamUseCase,
    private val getTeamUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamUseCase,
    private val deleteTeamUseCase: com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase,
    uiEventHandler: UiEventHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(), UiEventHandler by uiEventHandler {

    private var originalModel: com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel? = null

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _icon = MutableStateFlow<TeamIcon?>(null)
    val icon: StateFlow<TeamIcon?> = _icon.asStateFlow()

    private val _iconChanging = MutableStateFlow(false)
    val iconChanging: StateFlow<Boolean> = _iconChanging.asStateFlow()

    private val _isNewEntity = MutableStateFlow(true)
    val isNewEntity = _isNewEntity.asStateFlow()

    val valid: StateFlow<Boolean> = title.map {
        it.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        savedStateHandle.get<Int>(ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: run {
            _icon.value = TeamIcon.entries.toTypedArray().let {
                it[Random.nextInt(it.size)]
            }
        }
    }

    private fun initWithId(id: Int) = viewModelScope.launch {
        originalModel = getTeamUseCase(id)?.also {
            _title.value = it.title
            _description.value = it.description
            _icon.value = it.icon
            _isNewEntity.value = false
        }

    }

    fun onConfirm() {
        if (valid.value) {
            viewModelScope.launch {
                insertTeamUseCase(
                    com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel(
                        id = originalModel?.id,
                        title = title.value,
                        description = description.value,
                        icon = icon.value!!
                    )
                )
            }
        }
        sendUiEvent(UiEvent.Done)
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onDelete() = viewModelScope.launch {

        originalModel?.let {
            deleteTeamUseCase(it)
        }
        sendUiEvent(UiEvent.Done)
    }

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onIconEdit(changing: Boolean = true) {
        _iconChanging.value = changing
    }

    fun onIconChange(icon: TeamIcon) {
        _icon.value = icon
        _iconChanging.value = false
    }

}