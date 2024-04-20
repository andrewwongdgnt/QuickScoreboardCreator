package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.common.util.UiEvent
import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.usecase.DeleteScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.usecase.GetScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.usecase.InsertScoreboardListUseCase
import com.plcoding.mvvmtodoapp.util.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardListViewModel @Inject constructor(
    getScoreboardListUseCase: GetScoreboardListUseCase,
    private val insertScoreboardListUseCase: InsertScoreboardListUseCase,
    private val deleteScoreboardListUseCase: DeleteScoreboardListUseCase,
) : ViewModel() {
    private val scoreboardEntityList = getScoreboardListUseCase()
    val scoreboardList = scoreboardEntityList.map {
        it.map { e ->
            ScoreboardItemData(
                e.id, e.title, e.description
            )
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedScoreboardList: List<ScoreboardEntity>? = null

    fun onEvent(event: ScoreboardListEvent) {
        when (event) {

            is ScoreboardListEvent.OnAdd -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_SCOREBOARD))
            }

            is ScoreboardListEvent.OnEdit -> {
                sendUiEvent(UiEvent.Navigate("${Routes.ADD_EDIT_SCOREBOARD}?id=${event.scoreboard.id}"))
            }

            is ScoreboardListEvent.OnDelete -> {
                viewModelScope.launch {
                    scoreboardEntityList.first().filter { entity ->
                        entity.id in event.scoreboardList.map { it.id }
                    }.let { scoreboardEntityList ->
                        deletedScoreboardList = scoreboardEntityList
                        deleteScoreboardListUseCase(scoreboardEntityList)
                    }
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            message = "${event.scoreboardList.size} scoreboard(s) deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is ScoreboardListEvent.OnUndoDelete -> {
                deletedScoreboardList?.let { scoreboardList ->
                    viewModelScope.launch {
                        insertScoreboardListUseCase(scoreboardList)
                    }
                }
            }

            is ScoreboardListEvent.OnLaunch -> {
                // TODO complete this
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}