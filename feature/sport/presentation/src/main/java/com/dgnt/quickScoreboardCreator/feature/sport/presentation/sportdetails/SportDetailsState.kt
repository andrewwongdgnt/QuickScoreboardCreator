package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule

data class SportDetailsState(
    val title: String = "",
    val description: String = "",
    val iconState: SportIconState = SportIconState.Initial,
    val intervalLabel: String = "",
    val isNewEntity: Boolean = true,
    val winRule: WinRule = WinRule.Final,
    val intervalList: List<IntervalEditingInfo> = listOf(generateGenericIntervalInfo())
) {

    val valid
        get() =
            title.isNotBlank() && intervalLabel.isNotBlank() && intervalList.all { interval ->

                val checkingTimeLimitAndMaxScore = (interval.intervalData.increasing && interval.maxScoreInput.toIntOrNull()?.let { it > 0 } == true) || (!interval.intervalData.increasing && (interval.timeRepresentationPair.first.toIntOrNull()?.let { it > 0 } == true || interval.timeRepresentationPair.second.toIntOrNull()?.let { it > 0 } == true))

                val checkingInitialScoreInput = (interval.initialScoreInput.isNotBlank())

                val checkingIncrementList = interval.primaryIncrementInputList.all { it.isNotBlank() }

                val checkingPrimaryMapping = (interval.allowPrimaryMapping && interval.primaryMappingInputList.isNotEmpty() && interval.primaryMappingInputList.all { it.first.isNotBlank() && it.second.isNotBlank() }) || !interval.allowPrimaryMapping

                val checkingSecondaryScoring = (interval.allowSecondaryScore && interval.scoreInfo.secondaryScoreLabel.isNotBlank()) || !interval.allowSecondaryScore

                checkingTimeLimitAndMaxScore && checkingInitialScoreInput && checkingIncrementList && checkingPrimaryMapping && checkingSecondaryScoring
            }

    companion object {
        fun generateGenericIntervalInfo() =
            IntervalEditingInfo(
                scoreInfo = ScoreInfo(
                    scoreRule = ScoreRule.None,
                    scoreToDisplayScoreMap = mapOf(),
                    secondaryScoreLabel = "",
                    dataList = listOf(
                        generateDefaultScoreGroup(),
                        generateDefaultScoreGroup(),
                    )
                ),
                intervalData = IntervalData(
                    current = 0,
                    initial = 0,
                    increasing = false
                ),
                timeRepresentationPair = "0" to "0",
                maxScoreInput = "10",
                initialScoreInput = "0",
                primaryIncrementInputList = listOf("+1"),
                allowPrimaryMapping = false,
                primaryMappingInputList = listOf("0" to "0"),
                allowSecondaryScore = false
            )

        fun generateDefaultScoreGroup() = ScoreGroup(
            primary = ScoreData(
                current = 0,
                initial = 0,
                increments = listOf(1)
            ),
            secondary = null
        )
    }
}

sealed interface SportIconState {
    data object Initial : SportIconState
    sealed class Picked(val sportIcon: SportIcon) : SportIconState {
        class Changing(sportIcon: SportIcon) : Picked(sportIcon)
        class Displaying(sportIcon: SportIcon) : Picked(sportIcon)
    }
}