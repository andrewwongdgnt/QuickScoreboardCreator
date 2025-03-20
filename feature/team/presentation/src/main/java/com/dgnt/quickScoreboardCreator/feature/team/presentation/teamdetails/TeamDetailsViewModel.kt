package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.ID
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.DeleteTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.InsertTeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    private val insertTeamUseCase: InsertTeamUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val deleteTeamUseCase: DeleteTeamUseCase,
    uiEventHandler: UiEventHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(), UiEventHandler by uiEventHandler {

    private var originalModel: TeamModel? = null

    private val _state = MutableStateFlow(TeamDetailsState())
    val state: StateFlow<TeamDetailsState> = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>(ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: run {
            TeamIcon.entries.toTypedArray().let {
                it[Random.nextInt(it.size)]
            }.let { icon ->
                _state.update { state ->
                    state.copy(
                        iconState = TeamIconState.Picked.Displaying(icon)
                    )
                }
            }
        }
    }

    private fun initWithId(id: Int) = viewModelScope.launch {
        originalModel = getTeamUseCase(id)?.also {
            _state.update { state ->
                state.copy(
                    title = it.title,
                    description = it.description,
                    iconState = TeamIconState.Picked.Displaying(it.icon),
                    isNewEntity = false,
                )
            }
        }
    }

    fun onAction(action: TeamDetailsAction) {
        when (action) {
            TeamDetailsAction.Confirm -> onConfirm()
            TeamDetailsAction.Delete -> onDelete()
            is TeamDetailsAction.DescriptionChange -> onDescriptionChange(action.description)
            TeamDetailsAction.Dismiss -> onDismiss()
            is TeamDetailsAction.IconChange -> onIconChange(action.icon)
            is TeamDetailsAction.IconEdit -> onIconEdit(action.changing)
            is TeamDetailsAction.TitleChange -> onTitleChange(action.title)
        }
    }

    private fun onConfirm() {
        state.value.run {
            if (valid) {
                viewModelScope.launch {
                    insertTeamUseCase(
                        TeamModel(
                            id = originalModel?.id,
                            title = title,
                            description = description,
                            icon = (iconState as TeamIconState.Picked).teamIcon
                        )
                    )
                }
            }
            sendUiEvent(Done)
        }
    }

    private fun onDismiss() = sendUiEvent(Done)

    private fun onDelete() = viewModelScope.launch {
        originalModel?.let {
            deleteTeamUseCase(it)
        }
        sendUiEvent(Done)
    }

    private fun onTitleChange(title: String) {
        _state.update { state ->
            state.copy(
                title = title
            )
        }
    }

    private fun onDescriptionChange(description: String) {
        _state.update { state ->
            state.copy(
                description = description
            )
        }
    }

    private fun onIconEdit(changing: Boolean) {
        (state.value.iconState as? TeamIconState.Picked)?.teamIcon?.let { originalTeamIcon ->
            _state.update { state ->
                state.copy(
                    iconState = if (changing)
                        TeamIconState.Picked.Changing(originalTeamIcon)
                    else
                        TeamIconState.Picked.Displaying(originalTeamIcon)
                )
            }
        }
    }

    private fun onIconChange(icon: TeamIcon) {
        _state.update { state ->
            state.copy(
                iconState = TeamIconState.Picked.Displaying(icon)
            )
        }
    }

}