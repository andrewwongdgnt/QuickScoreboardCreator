package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardDetailsViewModel @Inject constructor(
    private val resources: Resources,
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
            initWithId(id)
        } ?: savedStateHandle.get<ScoreboardType>("type")?.let {
            initWithScoreboardType(it)
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
            getScoreboardUseCase(id)?.let {
                title = it.title
                description = it.description
                scoreboard = it
            }
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {
        title = resources.getString(scoreboardType.titleRes)
        description = resources.getString(scoreboardType.descriptionRes)
    }

    fun onEvent(event: ScoreboardDetailsEvent) {
        when (event) {
            is ScoreboardDetailsEvent.OnDone -> {
                viewModelScope.launch {
                    if (!validate()) {

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