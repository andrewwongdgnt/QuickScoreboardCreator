package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamlist

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamItemData

class TeamListPreviewStateProvider: CollectionPreviewParameterProvider<TeamListState> (
    listOf(
        TeamListState(
            categorizedTeamList = emptyList()
        ),
        TeamListState(
            categorizedTeamList = listOf(
                CategorizedTeamItemData(
                    "D",
                    listOf(
                        TeamItemData(0, "DGNT", "My Description 1", TeamIcon.TIGER),
                        TeamItemData(1, "Dragons", "My Description 2", TeamIcon.TIGER),
                        TeamItemData(2, "Darkness", "My Description 3", TeamIcon.TIGER)
                    )
                ),
                CategorizedTeamItemData(
                    "T",
                    listOf(
                        TeamItemData(3, "tricksters", "tricky people", TeamIcon.TIGER),
                        TeamItemData(5, "Terminators", "My Description 5", TeamIcon.TIGER)
                    )
                ),
                CategorizedTeamItemData(
                    "J",
                    listOf(
                        TeamItemData(4, "Jedi Council", "My Description 4", TeamIcon.TIGER)
                    )
                )

            )
        ),
    )
)