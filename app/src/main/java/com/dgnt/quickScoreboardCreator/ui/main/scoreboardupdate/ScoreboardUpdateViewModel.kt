package com.dgnt.quickScoreboardCreator.ui.main.scoreboardupdate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.common.util.UiEvent
import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.usecase.InsertScoreboardListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardUpdateViewModel @Inject constructor(
    private val insertScoreboardListUseCase: InsertScoreboardListUseCase,
    private val getScoreboardUseCase: GetScoreboardUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var scoreboard by mutableStateOf<ScoreboardEntity?>(null)
        private set

    var title by mutableStateOf("")

    var description by mutableStateOf("")


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>("id")?.takeUnless { it < 0 }?.let { id ->
            viewModelScope.launch {
                getScoreboardUseCase(id)?.let {
                    title = it.title
                    description = it.description
                    scoreboard = it
                }
            }
        }
    }

    fun onEvent(event: ScoreboardUpdateEvent) {
        when (event) {
            is ScoreboardUpdateEvent.OnUpdate -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "The title can't be empty"
                            )
                        )
                        return@launch
                    }
                    insertScoreboardListUseCase(
                        listOf(
                            ScoreboardEntity(
                                id = scoreboard?.id,
                                title = title,
                                description = description
                            )
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
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