package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportlist

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportListItem
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType

class SportListPreviewStateProvider : CollectionPreviewParameterProvider<SportListState>(
    listOf(
        SportListState(
            defaultSportList = listOf(SportType.BASKETBALL, SportType.HOCKEY, SportType.SPIKEBALL),
            customSportList = emptyList()
        ),
        SportListState(
            defaultSportList = listOf(SportType.BASKETBALL, SportType.HOCKEY, SportType.SPIKEBALL),
            customSportList = listOf(
                SportListItem(SportIdentifier.Custom(1), "My Sport 1", "My Description 1", SportIcon.BASKETBALL),
                SportListItem(SportIdentifier.Custom(2), "My Sport 2", "My Description 2", SportIcon.TENNIS),
                SportListItem(SportIdentifier.Custom(3), "My Sport 3", "My Description 3 ", SportIcon.BOXING),
            ),
        )
    )
)