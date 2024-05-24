package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardCategorizer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.DeleteScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
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
    private val deleteScoreboardUseCase: DeleteScoreboardUseCase,
    private val scoreboardCategorizer: ScoreboardCategorizer,
) : ViewModel() {
    private val scoreboardEntityList = getScoreboardListUseCase()
    val categorizedScoreboards = scoreboardEntityList.map { scoreboardEntityList ->
        scoreboardCategorizer(
            listOf(
                ScoreboardType.BASKETBALL,
                ScoreboardType.HOCKEY,
                ScoreboardType.SPIKEBALL,
                ScoreboardType.TENNIS,
            ),
            scoreboardEntityList
        )
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedScoreboardList: MutableList<ScoreboardEntity> = mutableListOf()

    fun onEvent(event: ScoreboardListEvent) {
        when (event) {

            ScoreboardListEvent.OnAdd -> {
                sendUiEvent(UiEvent.ScoreboardDetails())
            }

            is ScoreboardListEvent.OnEdit -> {
                sendUiEvent(UiEvent.ScoreboardDetails(event.id, event.type))
            }

            is ScoreboardListEvent.OnDelete -> {
                viewModelScope.launch {
                    scoreboardEntityList.first().find { entity ->
                        entity.id == event.id
                    }?.let {
                        deletedScoreboardList.add(it)
                        deleteScoreboardUseCase(it)
                    }
                    sendUiEvent(
                        UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                            message = R.plurals.deletedScoreboardMsg,
                            quantity = deletedScoreboardList.size,
                            action = R.string.undo
                        )
                    )
                }
            }

            ScoreboardListEvent.OnUndoDelete -> {
                val scoreboardList = deletedScoreboardList.toList()
                viewModelScope.launch {
                    insertScoreboardListUseCase(scoreboardList)
                }
                onEvent(ScoreboardListEvent.OnClearDeletedScoreboardList)
            }

            is ScoreboardListEvent.OnLaunch -> {
                sendUiEvent(UiEvent.LaunchScoreboard(event.id, event.type))
            }

            ScoreboardListEvent.OnClearDeletedScoreboardList -> {
                deletedScoreboardList.clear()
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}