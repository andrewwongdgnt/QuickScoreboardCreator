package com.dgnt.quickScoreboardCreator.feature.history.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.history.domain.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryItemData
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import org.joda.time.DateTime
import javax.inject.Inject

class CategorizeHistoryUseCase @Inject constructor() {
    operator fun invoke(historyModelList: List<HistoryModel>) =
        historyModelList.mapNotNull { e ->
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