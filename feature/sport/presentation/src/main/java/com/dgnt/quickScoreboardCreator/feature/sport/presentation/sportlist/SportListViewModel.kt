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

    fun onAdd() = sendUiEvent(SportDetails())

    fun onEdit(sportIdentifier: SportIdentifier) = sendUiEvent(SportDetails(sportIdentifier))

    fun onDelete(id: Int) = viewModelScope.launch {
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

    fun onUndoDelete() {
        deletedSportList.toList().takeUnless { it.isEmpty() }?.let { sportList ->
            viewModelScope.launch {
                insertSportListUseCase(sportList)
            }
            onClearDeletedSportList()
        }
    }

    fun onClearDeletedSportList() = deletedSportList.clear()

    fun onLaunch(sportIdentifier: SportIdentifier) = sendUiEvent(LaunchScoreboard(sportIdentifier))

}