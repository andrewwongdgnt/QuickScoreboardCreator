package com.dgnt.quickScoreboardCreator.ui.scoreboardlist

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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardListViewModel @Inject constructor(
    private val getScoreboardListUseCase: GetScoreboardListUseCase,
    private val insertScoreboardListUseCase: InsertScoreboardListUseCase,
    private val deleteScoreboardListUseCase: DeleteScoreboardListUseCase,
) : ViewModel() {
    val scoreboardList = getScoreboardListUseCase()

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
                    deletedScoreboardList = event.scoreboardList
                    deleteScoreboardListUseCase(event.scoreboardList)
                    sendUiEvent(
                        UiEvent.ShowSnackBar(
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