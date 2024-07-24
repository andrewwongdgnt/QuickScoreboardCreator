package com.dgnt.quickScoreboardCreator.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.history.model.CategorizedHistoryItemData

fun interface HistoryCategorizer {

    operator fun invoke(historyEntityList: List<HistoryEntity>): List<CategorizedHistoryItemData>

}