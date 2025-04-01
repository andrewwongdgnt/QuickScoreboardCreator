package com.dgnt.quickScoreboardCreator.feature.team.presentation.teamdetails

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon


class TeamDetailsPreviewStateProvider : CollectionPreviewParameterProvider<TeamDetailsState>(
    listOf(
        TeamDetailsState(
            title = "",
            description = "",
            iconState = TeamIconState.Picked.Changing(TeamIcon.GORILLA),
            isNewEntity = false
        ),
        TeamDetailsState(
            title = "Gorilla",
            description = "Big Ape",
            iconState = TeamIconState.Picked.Displaying(TeamIcon.GORILLA),
            isNewEntity = false,
            valid = true
        ),
        TeamDetailsState(
            title = "Tiger",
            description = "",
            iconState = TeamIconState.Picked.Displaying(TeamIcon.TIGER),
            isNewEntity = true,
            valid = true
        ),
        TeamDetailsState(
            title = "",
            description = "From planet X",
            iconState = TeamIconState.Picked.Displaying(TeamIcon.ALIEN),
            isNewEntity = false,
            valid = true
        ),
        TeamDetailsState(
            title = "",
            description = "",
            iconState = TeamIconState.Initial,
            isNewEntity = false
        ),

    )
)