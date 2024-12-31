package com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic

import com.dgnt.quickScoreboardCreator.feature.history.domain.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel

fun interface HistoryCategorizer {

    operator fun invoke(historyEntityList: List<HistoryModel>): List<CategorizedHistoryItemData>

}