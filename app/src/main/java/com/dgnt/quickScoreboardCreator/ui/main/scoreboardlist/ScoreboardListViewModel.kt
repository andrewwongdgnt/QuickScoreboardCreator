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
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardListViewModel @Inject constructor(
    getScoreboardListUseCase: GetScoreboardListUseCase,
    private val insertScoreboardListUseCase: InsertScoreboardListUseCase,
    private val deleteScoreboardUseCase: DeleteScoreboardUseCase,
    private val scoreboardCategorizer: ScoreboardCategorizer,
    uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
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

    private var deletedScoreboardList: MutableList<ScoreboardEntity> = mutableListOf()

    fun onAdd() = sendUiEvent(UiEvent.ScoreboardDetails())

    fun onEdit(scoreboardIdentifier: ScoreboardIdentifier) = sendUiEvent(UiEvent.ScoreboardDetails(scoreboardIdentifier))

    fun onDelete(id: Int) = viewModelScope.launch {
        scoreboardEntityList.first().find { entity ->
            entity.id == id
        }?.let {
            deletedScoreboardList.add(it)
            deleteScoreboardUseCase(it)
        }
        sendUiEvent(
            UiEvent.SnackBar.QuantitySnackBar(
                message = R.plurals.deletedScoreboardMsg,
                quantity = deletedScoreboardList.size,
                action = R.string.undo
            )
        )
    }

    fun onUndoDelete() {
        deletedScoreboardList.toList().takeUnless { it.isEmpty() }?.let { scoreboardList ->
            viewModelScope.launch {
                insertScoreboardListUseCase(scoreboardList)
            }
            onClearDeletedScoreboardList()
        }
    }

    fun onClearDeletedScoreboardList() = deletedScoreboardList.clear()

    fun onLaunch(scoreboardIdentifier: ScoreboardIdentifier) = sendUiEvent(UiEvent.LaunchScoreboard(scoreboardIdentifier))

}