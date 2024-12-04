package com.dgnt.quickScoreboardCreator.core.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.history.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel

fun interface HistoryCategorizer {

    operator fun invoke(historyEntityList: List<HistoryModel>): List<CategorizedHistoryItemData>

}