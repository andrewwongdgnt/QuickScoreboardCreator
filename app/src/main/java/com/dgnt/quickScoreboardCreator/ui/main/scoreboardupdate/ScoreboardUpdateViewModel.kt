package com.dgnt.quickScoreboardCreator.ui.main.scoreboardupdate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.common.util.UiEvent
import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.usecase.InsertScoreboardListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardUpdateViewModel @Inject constructor(
    private val insertScoreboardListUseCase: InsertScoreboardListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var scoreboard by mutableStateOf<ScoreboardEntity?>(null)
        private set

    var title by mutableStateOf("")

    var description by mutableStateOf("")


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val id = savedStateHandle.get<Int>("id")!!
        if (id != -1) {
            //TODO figure out how to get these without the ID
            title = "Edited Title"
            description = "Edited Description"
            scoreboard = ScoreboardEntity(title = title, description = description)


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