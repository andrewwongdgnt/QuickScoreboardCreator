package com.dgnt.quickScoreboardCreator.feature.history.presentation.historydetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.DeleteHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class HistoryDetailsViewModel @Inject constructor(
    private val insertHistoryUseCase: InsertHistoryUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val uiEventHandler: UiEventHandler,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), UiEventHandler by uiEventHandler {

    private var originalModel: HistoryModel? = null

    private val _state = MutableStateFlow(HistoryDetailsState())
    val state: StateFlow<HistoryDetailsState> = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>(NavArguments.ID)?.let {
            initWithId(it)
        }
    }

    private fun initWithId(it: Int) = viewModelScope.launch {
        originalModel = getHistoryUseCase(it)?.also {
            _state.update { state ->
                state.copy(
                    title = it.title,
                    description = it.description,
                    iconState = HistoryIconState.Picked.Displaying(it.icon),
                )
            }
        }
    }

    fun onAction(action: HistoryDetailsAction) {
        when (action) {
            HistoryDetailsAction.Confirm -> onConfirm()
            HistoryDetailsAction.Delete -> onDelete()
            is HistoryDetailsAction.DescriptionChange -> onDescriptionChange(action.description)
            HistoryDetailsAction.Dismiss -> onDismiss()
            is HistoryDetailsAction.IconChange -> onIconChange(action.icon)
            is HistoryDetailsAction.IconEdit -> onIconEdit(action.changing)
            is HistoryDetailsAction.TitleChange -> onTitleChange(action.title)
        }
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

    private fun onDismiss() = sendUiEvent(Done)

    private fun onDelete() = viewModelScope.launch {

        originalModel?.let {
            deleteHistoryUseCase(it)
        }
        sendUiEvent(Done)
    }

    private fun onIconEdit(changing: Boolean) {
        (state.value.iconState as? HistoryIconState.Picked)?.sportIcon?.let { originalSportIcon ->
            _state.update { state ->
                state.copy(
                    iconState = if (changing)
                        HistoryIconState.Picked.Changing(originalSportIcon)
                    else
                        HistoryIconState.Picked.Displaying(originalSportIcon)
                )
            }
        }
    }

    private fun onIconChange(icon: SportIcon) {
        _state.update { state ->
            state.copy(
                iconState = HistoryIconState.Picked.Displaying(icon)
            )
        }
    }

    private fun onConfirm() {
        state.value.run {
            if (valid) {
                viewModelScope.launch {
                    insertHistoryUseCase(
                        HistoryModel(
                            id = originalModel?.id,
                            title = title,
                            description = description,
                            icon = (iconState as HistoryIconState.Picked).sportIcon,
                            lastModified = DateTime.now(),
                            createdAt = originalModel?.createdAt ?: DateTime.now(),
                            historicalScoreboard = originalModel?.historicalScoreboard!!,
                            temporary = false
                        )
                    )
                }
            }
            sendUiEvent(Done)
        }
    }

}