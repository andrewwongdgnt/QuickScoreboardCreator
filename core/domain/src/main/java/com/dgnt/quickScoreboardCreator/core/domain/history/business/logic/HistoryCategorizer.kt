package com.dgnt.quickScoreboardCreator.core.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.history.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity

fun interface HistoryCategorizer {

    operator fun invoke(historyEntityList: List<HistoryEntity>): List<CategorizedHistoryItemData>

}