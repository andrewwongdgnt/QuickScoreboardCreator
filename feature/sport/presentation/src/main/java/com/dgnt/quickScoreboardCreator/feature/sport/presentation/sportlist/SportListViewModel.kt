package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportType
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.CategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.DeleteSportUseCase
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.GetSportListUseCase
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.InsertSportListUseCase
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
    val categorizedSports = sportModelList.map { sportModelList ->
        categorizeSportUseCase(
            listOf(
                SportType.BASKETBALL,
                SportType.HOCKEY,
                SportType.SPIKEBALL,
                SportType.TENNIS,
                SportType.BOXING,
            ),
            sportModelList
        )
    }

    private var deletedSportList: MutableList<SportModel> = mutableListOf()

    fun onAdd() = sendUiEvent(UiEvent.SportDetails())

    fun onEdit(sportIdentifier: com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier) = sendUiEvent(UiEvent.SportDetails(sportIdentifier))

    fun onDelete(id: Int) = viewModelScope.launch {
        sportModelList.first().find { model ->
            (model.sportIdentifier as? com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier.Custom)?.id == id
        }?.let {
            deletedSportList.add(it)
            deleteSportUseCase(it)
        }
        sendUiEvent(
            UiEvent.SnackBar.QuantitySnackBar(
                message = R.plurals.deletedSportMsg,
                quantity = deletedSportList.size,
                action = R.string.undo
            )
        )
    }

    fun onUndoDelete() {
        deletedSportList.toList().takeUnless { it.isEmpty() }?.let { sportList ->
            viewModelScope.launch {
                insertSportListUseCase(sportList)
            }
            onClearDeletedSportList()
        }
    }

    fun onClearDeletedSportList() = deletedSportList.clear()

    fun onLaunch(sportIdentifier: com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier) = sendUiEvent(UiEvent.LaunchScoreboard(sportIdentifier))

}