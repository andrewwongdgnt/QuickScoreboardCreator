package com.dgnt.quickScoreboardCreator.ui.main.historydetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.history.usecase.DeleteHistoryUseCase
import com.dgnt.quickScoreboardCreator.domain.history.usecase.GetHistoryUseCase
import com.dgnt.quickScoreboardCreator.domain.history.usecase.InsertHistoryUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.ui.common.Arguments
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private var originalEntity: HistoryEntity? = null

    private var _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private var _icon = MutableStateFlow<ScoreboardIcon?>(null)
    val icon = _icon.asStateFlow()

    private val _iconChanging = MutableStateFlow(false)
    val iconChanging: StateFlow<Boolean> = _iconChanging.asStateFlow()

    val valid: StateFlow<Boolean> = title.map {
        it.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        savedStateHandle.get<Int>(Arguments.ID)?.let {
            initWithId(it)
        }
    }

    private fun initWithId(it: Int) = viewModelScope.launch {
        originalEntity = getHistoryUseCase(it)?.also {
            _title.value = it.title
            _description.value = it.description
            _icon.value = it.icon
        }
    }

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onDelete() = viewModelScope.launch {

        originalEntity?.let {
            deleteHistoryUseCase(it)
        }
        sendUiEvent(UiEvent.Done)
    }

    fun onIconEdit(changing: Boolean = true) {
        _iconChanging.value = changing
    }

    fun onIconChange(icon: ScoreboardIcon) {
        _icon.value = icon
        _iconChanging.value = false
    }

    fun onConfirm() {
        if (valid.value) {
            viewModelScope.launch {
                insertHistoryUseCase(
                    HistoryEntity(
                        id = originalEntity?.id,
                        title = title.value,
                        description = description.value,
                        icon = icon.value!!,
                        lastModified = DateTime.now(),
                        createdAt = originalEntity?.createdAt ?: DateTime.now(),
                        historicalScoreboard = originalEntity?.historicalScoreboard!!,
                        temporary = false
                    )
                )
            }
        }
        sendUiEvent(UiEvent.Done)
    }

}