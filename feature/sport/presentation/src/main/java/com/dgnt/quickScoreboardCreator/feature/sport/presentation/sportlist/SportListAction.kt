package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier

sealed interface SportListAction {
    data class Launch(val sportIdentifier: SportIdentifier) : SportListAction
    data object Add : SportListAction
    data class Edit(val sportIdentifier: SportIdentifier) : SportListAction
    data class Delete(val id: Int) : SportListAction
    data object UndoDelete : SportListAction
    data object ClearDeletedSportList : SportListAction
}