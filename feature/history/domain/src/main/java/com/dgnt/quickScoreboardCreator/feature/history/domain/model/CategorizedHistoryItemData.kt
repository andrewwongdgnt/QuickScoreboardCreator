package com.dgnt.quickScoreboardCreator.feature.history.domain.model

import org.joda.time.DateTime

data class CategorizedHistoryItemData(
    val dateTime: DateTime,
    val historyItemDataList: List<HistoryItemData>
)