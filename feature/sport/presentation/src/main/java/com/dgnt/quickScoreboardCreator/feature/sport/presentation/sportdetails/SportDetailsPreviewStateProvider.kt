package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule

class SportDetailsPreviewStateProvider: CollectionPreviewParameterProvider<SportDetailsState>(
    listOf(
        SportDetailsState(
            title = "",
            description = "",
            iconState = SportIconState.Picked.Changing(SportIcon.BASKETBALL),
            isNewEntity = false
        ),
        SportDetailsState(
            title = "Basketball",
            description = "NBA",
            iconState = SportIconState.Picked.Displaying(SportIcon.BASKETBALL),
            isNewEntity = false
        ),
        SportDetailsState(
            title = "Hockey",
            description = "",
            iconState = SportIconState.Picked.Displaying(SportIcon.HOCKEY),
            isNewEntity = false,
            winRule = WinRule.Count,
            intervalLabel = "Period"
        ),
        SportDetailsState(
            title = "",
            description = "my description",
            iconState = SportIconState.Initial,
            isNewEntity = false
        ),
        SportDetailsState(
            title = "Elite Boxing",
            description = "",
            iconState = SportIconState.Picked.Displaying(SportIcon.BOXING),
            intervalLabel = "Round",
            isNewEntity = true,
            intervalList = listOf(
                IntervalEditingInfo(
                    scoreInfo = ScoreInfo(
                        scoreRule = ScoreRule.None,
                        scoreToDisplayScoreMap = mapOf(),
                        secondaryScoreLabel = "",
                        dataList = listOf()
                    ),
                    intervalData = IntervalData(
                        current = 0,
                        initial = 0,
                        increasing = false,
                        soundEffect = IntervalEndSound.Bell
                    ),
                    timeRepresentationPair = Pair("9", "24"),
                    maxScoreInput = "33",
                    initialScoreInput = "10",
                    primaryIncrementInputList = listOf("+1"),
                    allowPrimaryMapping = false,
                    primaryMappingInputList = listOf(),
                    allowSecondaryScore = false
                ),
            )
        ),
    )
)