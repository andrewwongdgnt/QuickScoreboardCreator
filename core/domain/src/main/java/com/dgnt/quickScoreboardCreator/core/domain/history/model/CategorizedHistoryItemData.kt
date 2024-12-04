package com.dgnt.quickScoreboardCreator.core.domain.history.model

import org.joda.time.DateTime

data class CategorizedHistoryItemData(
    val dateTime: DateTime,
    val historyItemDataList: List<HistoryItemData>
)