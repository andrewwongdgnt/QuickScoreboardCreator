package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule

sealed interface SportDetailsAction {
    data object Confirm : SportDetailsAction
    data object Dismiss : SportDetailsAction
    data object Delete : SportDetailsAction
    data class TitleChange(val title: String) : SportDetailsAction
    data class DescriptionChange(val description: String) : SportDetailsAction
    data class WinRuleChange(val winRule: WinRule) : SportDetailsAction
    data class IconEdit(val changing: Boolean) : SportDetailsAction
    data class IconChange(val icon: SportIcon) : SportDetailsAction
    data class IntervalLabelChange(val intervalLabel: String) : SportDetailsAction
    data class IntervalAdd(val index: Int? = null) : SportDetailsAction
    data class IntervalRemove(val index: Int) : SportDetailsAction
    data class IntervalMove(val up: Boolean, val index: Int) : SportDetailsAction
    data class IntervalEditForSoundEffect(val index: Int, val soundEffect: IntervalEndSound) : SportDetailsAction
    data class IntervalEditForTimeIsIncreasing(val index: Int, val timeIsIncreasing: Boolean) : SportDetailsAction
    data class IntervalEditForMinute(val index: Int, val rawValue: String) : SportDetailsAction
    data class IntervalEditForSecond(val index: Int, val rawValue: String) : SportDetailsAction
    data class IntervalEditForMaxScoreInput(val index: Int, val rawValue: String) : SportDetailsAction
    data class IntervalEditForAllowDeuceAdv(val index: Int, val allow: Boolean) : SportDetailsAction
    data class IntervalEditForTeamCount(val index: Int, val teamCount: Int) : SportDetailsAction
    data class IntervalEditForInitialScoreInput(val index: Int, val rawValue: String) : SportDetailsAction
    data class IntervalEditForPrimaryIncrementAdd(val index: Int) : SportDetailsAction
    data class IntervalEditForPrimaryIncrement(val index: Int, val incrementIndex: Int, val value: String) : SportDetailsAction
    data class IntervalEditForPrimaryIncrementMove(val index: Int, val incrementIndex: Int, val up: Boolean): SportDetailsAction
    data class IntervalEditForPrimaryIncrementRemove(val index: Int, val incrementIndex: Int): SportDetailsAction
    data class IntervalEditForPrimaryIncrementRefresh(val index: Int, val incrementIndex: Int): SportDetailsAction
    data class IntervalEditForPrimaryMappingAllowed(val index: Int, val allowed: Boolean): SportDetailsAction
    data class IntervalEditForPrimaryMappingAdd(val index: Int): SportDetailsAction
    data class IntervalEditForPrimaryMappingOriginalScore(val index: Int, val mappingIndex: Int, val rawValue: String): SportDetailsAction
    data class IntervalEditForPrimaryMappingDisplayScore(val index: Int, val mappingIndex: Int, val value: String): SportDetailsAction
    data class IntervalEditForPrimaryMappingMove(val index: Int, val mappingIndex: Int, val up: Boolean): SportDetailsAction
    data class IntervalEditForPrimaryMappingRemove(val index: Int, val mappingIndex: Int): SportDetailsAction
    data class IntervalEditForSecondaryScoreAllowed(val index: Int, val allowed: Boolean): SportDetailsAction
    data class IntervalEditForSecondaryScoreLabel(val index: Int, val value: String): SportDetailsAction
}