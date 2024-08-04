package com.dgnt.quickScoreboardCreator.domain.history.business.logic

import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.history.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoryItemData
import org.joda.time.DateTime

class QSCHistoryCategorizer : HistoryCategorizer {
    override fun invoke(historyEntityList: List<HistoryEntity>) =
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