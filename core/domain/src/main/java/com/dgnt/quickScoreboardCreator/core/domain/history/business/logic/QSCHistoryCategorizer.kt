package com.dgnt.quickScoreboardCreator.core.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.history.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryItemData
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import org.joda.time.DateTime

class QSCHistoryCategorizer : HistoryCategorizer {
    override fun invoke(historyEntityList: List<HistoryModel>) =
        historyEntityList.mapNotNull { e ->
            e.id?.let { id ->
                HistoryItemData(
                    id, e.title, e.description, e.icon, e.lastModified, e.createdAt
                )
            }
        }.let { historyList ->
            historyList.groupBy { history ->
                history.lastModified.let {
                    DateTime(it.year, it.monthOfYear, 1, 0, 0)
                }
            }
                .toSortedMap(reverseOrder())
                .map { entry ->
                    CategorizedHistoryItemData(entry.key, entry.value.sortedByDescending { it.lastModified })
                }
        }


}