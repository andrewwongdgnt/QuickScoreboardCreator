package com.dgnt.quickScoreboardCreator.feature.history.presentation.historylist

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.CategorizedHistoryItemData
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryItemData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import org.joda.time.DateTime

class HistoryListPreviewStateProvider : CollectionPreviewParameterProvider<HistoryListState>(
    listOf(
        HistoryListState(
            categorizedHistoryList = emptyList()
        ),
        HistoryListState(
            categorizedHistoryList = listOf(
                CategorizedHistoryItemData(
                    DateTime(2024, 6, 1, 0, 0),
                    listOf(
                        HistoryItemData(1, "Tennis", "tennis_description", SportIcon.TENNIS, DateTime(2024, 6, 2, 10, 0), DateTime(2024, 6, 2, 10, 0)),
                        HistoryItemData(2, "Tennis", "another banger", SportIcon.TENNIS, DateTime(2024, 6, 1, 9, 0), DateTime(2024, 6, 1, 9, 0)),
                    )
                ),
                CategorizedHistoryItemData(
                    DateTime(2024, 5, 1, 0, 0),
                    listOf(
                        HistoryItemData(4, "Basketball NBA", "cool", SportIcon.BASKETBALL, DateTime(2024, 5, 2, 14, 0), DateTime(2024, 5, 2, 14, 0)),
                        HistoryItemData(7, "Hockey NHL", "", SportIcon.HOCKEY, DateTime(2024, 5, 2, 10, 0), DateTime(2024, 5, 2, 10, 0)),
                    )
                ),
                CategorizedHistoryItemData(
                    DateTime(2024, 1, 1, 0, 0),
                    listOf(
                        HistoryItemData(8, "Pick up bball", "f", SportIcon.BASKETBALL, DateTime(2024, 1, 31, 16, 14), DateTime(2024, 1, 31, 16, 14)),
                        HistoryItemData(55, "Pick up bball", "f2", SportIcon.BASKETBALL, DateTime(2024, 1, 22, 16, 14), DateTime(2024, 1, 22, 16, 14)),
                        HistoryItemData(83, "Pick up bball", "f", SportIcon.BASKETBALL, DateTime(2024, 1, 21, 16, 14), DateTime(2024, 1, 21, 16, 14)),
                    )
                )

            )
        )
    )
)