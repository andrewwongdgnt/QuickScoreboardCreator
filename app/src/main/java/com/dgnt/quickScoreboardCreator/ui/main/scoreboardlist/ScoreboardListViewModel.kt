package com.dgnt.quickScoreboardCreator.ui.main.scoreboardlist

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardCategorizer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.DeleteScoreboardListUseCase
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
    private val resources: Resources,
    getScoreboardListUseCase: GetScoreboardListUseCase,
    private val insertScoreboardListUseCase: InsertScoreboardListUseCase,
    private val deleteScoreboardListUseCase: DeleteScoreboardListUseCase,
    private val scoreboardLoader: ScoreboardLoader,
    private val scoreboardCategorizer: ScoreboardCategorizer,
) : ViewModel() {
    private val scoreboardEntityList = getScoreboardListUseCase()
    val categorizedScoreboards = scoreboardEntityList.map { scoreboardEntityList ->
        scoreboardCategorizer(
            listOf(R.raw.basketball, R.raw.hockey, R.raw.spikeball).map {
                scoreboardLoader(resources.openRawResource(it)) as DefaultScoreboardConfig
            }.map {
                it.scoreboardType
            },
            scoreboardEntityList
        )
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedScoreboardList: List<ScoreboardEntity>? = null

    fun onEvent(event: ScoreboardListEvent) {
        when (event) {

            is ScoreboardListEvent.OnAdd -> {
                sendUiEvent(UiEvent.ScoreboardDetails())
            }

            is ScoreboardListEvent.OnEdit -> {
                sendUiEvent(UiEvent.ScoreboardDetails(event.scoreboard.id, event.scoreboard.type))
            }

            is ScoreboardListEvent.OnDelete -> {
                viewModelScope.launch {
                    scoreboardEntityList.first().find { entity ->
                        entity.id == event.id
                    }?.let { scoreboardEntityToDelete ->
                        //TODO delete one thing instead of a list
                        val list = listOf(scoreboardEntityToDelete)
                        deletedScoreboardList = list
                        deleteScoreboardListUseCase(list)
                    }
                    sendUiEvent(
                        UiEvent.ShowSnackbar.ShowQuantitySnackbar(
                            message = R.plurals.deletedScoreboardMsg,
                            quantity = 1,
                            action = R.string.undo
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
                sendUiEvent(UiEvent.LaunchScoreboard(event.scoreboard.id, event.scoreboard.type))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}