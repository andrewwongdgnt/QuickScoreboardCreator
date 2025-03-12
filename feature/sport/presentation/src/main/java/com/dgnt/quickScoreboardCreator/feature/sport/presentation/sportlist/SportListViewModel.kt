package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.SnackBar
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.CategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.DeleteSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.GetSportListUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.InsertSportListUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent.LaunchScoreboard
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent.SportDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SportListViewModel @Inject constructor(
    getSportListUseCase: GetSportListUseCase,
    private val insertSportListUseCase: InsertSportListUseCase,
    private val deleteSportUseCase: DeleteSportUseCase,
    private val categorizeSportUseCase: CategorizeSportUseCase,
    uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {
    private val sportModelList = getSportListUseCase()
    val state = sportModelList.map { sportModelList ->
        categorizeSportUseCase(
            listOf(
                SportType.BASKETBALL,
                SportType.HOCKEY,
                SportType.SPIKEBALL,
                SportType.TENNIS,
                SportType.BOXING,
            ),
            sportModelList
        ).let { (defaultSportList, customSportList) ->
            SportListState(defaultSportList, customSportList)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, SportListState())

    private var deletedSportList: MutableList<SportModel> = mutableListOf()

    fun onAction(action: SportListAction){
        when (action)  {
            SportListAction.Add -> onAdd()
            SportListAction.ClearDeletedSportList -> onClearDeletedSportList()
            is SportListAction.Delete -> onDelete(action.id)
            is SportListAction.Edit -> onEdit(action.sportIdentifier)
            is SportListAction.Launch -> onLaunch(action.sportIdentifier)
            SportListAction.UndoDelete -> onUndoDelete()
        }
    }

    private fun onAdd() = sendUiEvent(SportDetails())

    private fun onEdit(sportIdentifier: SportIdentifier) = sendUiEvent(SportDetails(sportIdentifier))

    private fun onDelete(id: Int) = viewModelScope.launch {
        sportModelList.first().find { model ->
            (model.sportIdentifier as? SportIdentifier.Custom)?.id == id
        }?.let {
            deletedSportList.add(it)
            deleteSportUseCase(it)
        }
        sendUiEvent(
            SnackBar.QuantitySnackBar(
                message = R.plurals.deletedSportMsg,
                quantity = deletedSportList.size,
                action = R.string.undo
            )
        )
    }

    private fun onUndoDelete() {
        deletedSportList.toList().takeUnless { it.isEmpty() }?.let { sportList ->
            viewModelScope.launch {
                insertSportListUseCase(sportList)
            }
            onClearDeletedSportList()
        }
    }

    private fun onClearDeletedSportList() = deletedSportList.clear()

    private fun onLaunch(sportIdentifier: SportIdentifier) = sendUiEvent(LaunchScoreboard(sportIdentifier))

}