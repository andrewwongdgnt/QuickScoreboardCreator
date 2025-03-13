package com.dgnt.quickScoreboardCreator.feature.history.presentation.historydetails

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon

class HistoryDetailsPreviewStateProvider : CollectionPreviewParameterProvider<HistoryDetailsState>(
    listOf(
        HistoryDetailsState(
            title = "my title",
            description = "description 222",
            iconState = HistoryIconState.Picked.Changing(SportIcon.HOCKEY),
        ),
        HistoryDetailsState(
            iconState = HistoryIconState.Picked.Displaying(SportIcon.HOCKEY),
        ),
        HistoryDetailsState(
            title = "my title 333",
            iconState = HistoryIconState.Initial,
        )
    )
)