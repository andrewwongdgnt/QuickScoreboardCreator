package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.SPORT_IDENTIFIER
import com.dgnt.quickScoreboardCreator.core.presentation.ui.asIncrementDisplay
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.Done
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.core.util.swap
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.DeleteSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.GetSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.InsertSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.TimeConversionUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.descriptionRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.intervalLabelRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.rawRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.secondaryScoreLabelRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.titleRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails.SportDetailsState.Companion.generateDefaultScoreGroup
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails.SportDetailsState.Companion.generateGenericIntervalInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SportDetailsViewModel @Inject constructor(
    private val resources: Resources,
    private val insertSportUseCase: InsertSportUseCase,
    private val getSportUseCase: GetSportUseCase,
    private val deleteSportUseCase: DeleteSportUseCase,
    private val timeConversionUseCase: TimeConversionUseCase,
    savedStateHandle: SavedStateHandle,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {

    companion object {
        const val MIN_TEAMS = 2
        const val MAX_TEAMS = 8
        const val MAX_INCREMENTS_COUNT = 3
    }

    private var originalModel: SportModel? = null

    private var _state = MutableStateFlow(SportDetailsState())
    val state: StateFlow<SportDetailsState> = _state.asStateFlow()

    init {
        savedStateHandle.get<SportIdentifier?>(SPORT_IDENTIFIER)?.let { sId ->
            when (sId) {
                is SportIdentifier.Custom -> initWithId(sId.id)
                is SportIdentifier.Default -> initWithSportType(sId.sportType)
            }
        } ?: run {
            _state.value = state.value.copy(
                iconState = SportIconState.Picked.Displaying(
                    SportIcon.entries.toTypedArray().let {
                        it[Random.nextInt(it.size)]
                    }
                )
            )
        }

    }

    private fun initWithId(id: Int) = viewModelScope.launch {
        originalModel = getSportUseCase(id)?.also {
            _state.value = state.value.copy(
                title = it.title,
                description = it.description,
                winRule = it.winRule,
                iconState = SportIconState.Picked.Displaying(it.icon),
                intervalLabel = it.intervalLabel,
                isNewEntity = false
            )
        }
    }

    private fun initWithSportType(sportType: SportType) {
        getSportUseCase(resources.openRawResource(sportType.rawRes()))?.let {
            _state.value = state.value.copy(
                title = resources.getString(sportType.titleRes()),
                description = resources.getString(sportType.descriptionRes()),
                intervalLabel = resources.getString(sportType.intervalLabelRes()),
                isNewEntity = true,
                winRule = it.winRule,
                iconState = SportIconState.Picked.Displaying(it.icon),
                intervalList = it.intervalList.map { interval ->
                    //TODO should make a mapper for this
                    val scoreInfo = interval.first.copy(secondaryScoreLabel = resources.getString(sportType.secondaryScoreLabelRes()))
                    val intervalData = interval.second
                    IntervalEditingInfo(
                        scoreInfo = scoreInfo,
                        intervalData = intervalData,
                        timeRepresentationPair = timeConversionUseCase.toTimeData(intervalData.initial).run {
                            Pair(minute.toString(), second.toString())
                        },
                        maxScoreInput = (scoreInfo.scoreRule as? ScoreRule.Trigger)?.trigger?.toString() ?: "",
                        initialScoreInput = scoreInfo.dataList.firstOrNull()?.primary?.initial?.toString() ?: "",
                        primaryIncrementInputList = scoreInfo.dataList.firstOrNull()?.primary?.increments?.map { it.asIncrementDisplay() } ?: listOf("+1"),
                        allowPrimaryMapping = scoreInfo.scoreToDisplayScoreMap.isNotEmpty(),
                        primaryMappingInputList = scoreInfo.scoreToDisplayScoreMap.map { it.key.toString() to it.value },
                        allowSecondaryScore = scoreInfo.dataList.firstOrNull()?.secondary != null
                    )
                }
            )
        }
    }

    fun onAction(action: SportDetailsAction) {
        when (action) {
            SportDetailsAction.Confirm -> onConfirm()
            SportDetailsAction.Dismiss -> onDismiss()
            SportDetailsAction.Delete -> onDelete()
            is SportDetailsAction.DescriptionChange -> onDescriptionChange(action.description)
            is SportDetailsAction.IconChange -> onIconChange(action.icon)
            is SportDetailsAction.IconEdit -> onIconEdit(action.changing)
            is SportDetailsAction.IntervalAdd -> onIntervalAdd(action.index)
            is SportDetailsAction.IntervalEditForAllowDeuceAdv -> onIntervalEditForAllowDeuceAdv(action.index, action.allow)
            is SportDetailsAction.IntervalEditForInitialScoreInput -> onIntervalEditForInitialScoreInput(action.index, action.rawValue)
            is SportDetailsAction.IntervalEditForMaxScoreInput -> onIntervalEditForMaxScoreInput(action.index, action.rawValue)
            is SportDetailsAction.IntervalEditForMinute -> onIntervalEditForMinute(action.index, action.rawValue)
            is SportDetailsAction.IntervalEditForPrimaryIncrement -> onIntervalEditForPrimaryIncrement(action.index, action.incrementIndex, action.value)
            is SportDetailsAction.IntervalEditForPrimaryIncrementAdd -> onIntervalEditForPrimaryIncrementAdd(action.index)
            is SportDetailsAction.IntervalEditForPrimaryIncrementMove -> onIntervalEditForPrimaryIncrementMove(action.index, action.incrementIndex, action.up)
            is SportDetailsAction.IntervalEditForPrimaryIncrementRefresh -> onIntervalEditForPrimaryIncrementRefresh(action.index, action.incrementIndex)
            is SportDetailsAction.IntervalEditForPrimaryIncrementRemove -> onIntervalEditForPrimaryIncrementRemove(action.index, action.incrementIndex)
            is SportDetailsAction.IntervalEditForPrimaryMappingAdd -> onIntervalEditForPrimaryMappingAdd(action.index)
            is SportDetailsAction.IntervalEditForPrimaryMappingAllowed -> onIntervalEditForPrimaryMappingAllowed(action.index, action.allowed)
            is SportDetailsAction.IntervalEditForPrimaryMappingDisplayScore -> onIntervalEditForPrimaryMappingDisplayScore(action.index, action.mappingIndex, action.value)
            is SportDetailsAction.IntervalEditForPrimaryMappingMove -> onIntervalEditForPrimaryMappingMove(action.index, action.mappingIndex, action.up)
            is SportDetailsAction.IntervalEditForPrimaryMappingOriginalScore -> onIntervalEditForPrimaryMappingOriginalScore(action.index, action.mappingIndex, action.rawValue)
            is SportDetailsAction.IntervalEditForPrimaryMappingRemove -> onIntervalEditForPrimaryMappingRemove(action.index, action.mappingIndex)
            is SportDetailsAction.IntervalEditForSecond -> onIntervalEditForSecond(action.index, action.rawValue)
            is SportDetailsAction.IntervalEditForSecondaryScoreAllowed -> onIntervalEditForSecondaryScoreAllowed(action.index, action.allowed)
            is SportDetailsAction.IntervalEditForSecondaryScoreLabel -> onIntervalEditForSecondaryScoreLabel(action.index, action.value)
            is SportDetailsAction.IntervalEditForSoundEffect -> onIntervalEditForSoundEffect(action.index, action.soundEffect)
            is SportDetailsAction.IntervalEditForTeamCount -> onIntervalEditForTeamCount(action.index, action.teamCount)
            is SportDetailsAction.IntervalEditForTimeIsIncreasing -> onIntervalEditForTimeIsIncreasing(action.index, action.timeIsIncreasing)
            is SportDetailsAction.IntervalLabelChange -> onIntervalLabelChange(action.intervalLabel)
            is SportDetailsAction.IntervalMove -> onIntervalMove(action.up, action.index)
            is SportDetailsAction.IntervalRemove -> onIntervalRemove(action.index)
            is SportDetailsAction.TitleChange -> onTitleChange(action.title)
            is SportDetailsAction.WinRuleChange -> onWinRuleChange(action.winRule)
        }
    }

    private fun onConfirm() {
        state.value.run {
            if (valid) {
                viewModelScope.launch {
                    insertSportUseCase(
                        SportModel(
                            sportIdentifier = originalModel?.sportIdentifier,
                            title = title,
                            description = description,
                            winRule = winRule,
                            icon = (iconState as SportIconState.Picked).sportIcon,
                            intervalLabel = intervalLabel,
                            intervalList = listOf()
                        )
                    )
                }
            }
            sendUiEvent(Done)
        }
    }

    private fun onDismiss() = sendUiEvent(Done)

    private fun onDelete() = viewModelScope.launch {
        originalModel?.let {
            deleteSportUseCase(it)
        }
        sendUiEvent(Done)
    }

    private fun onTitleChange(title: String) {
        _state.value = state.value.copy(
            title = title
        )
    }

    private fun onDescriptionChange(description: String) {
        _state.value = state.value.copy(
            description = description
        )
    }

    private fun onWinRuleChange(winRule: WinRule) {
        _state.value = state.value.copy(
            winRule = winRule
        )
    }

    private fun onIconEdit(changing: Boolean) {
        (state.value.iconState as? SportIconState.Picked)?.sportIcon?.let { originalSportIcon ->
            _state.value = state.value.copy(
                iconState = if (changing)
                    SportIconState.Picked.Changing(originalSportIcon)
                else
                    SportIconState.Picked.Displaying(originalSportIcon)
            )
        }
    }

    private fun onIconChange(icon: SportIcon) {
        _state.value = state.value.copy(
            iconState = SportIconState.Picked.Displaying(icon)
        )
    }

    private fun onIntervalLabelChange(intervalLabel: String) {
        _state.value = state.value.copy(
            intervalLabel = intervalLabel
        )
    }

    private fun onIntervalAdd(index: Int? = null) {
        val intervalListValue = state.value.intervalList.toMutableList()
        if (index == null) {
            intervalListValue.add(generateGenericIntervalInfo())
        } else if (index in 0..intervalListValue.size) {
            intervalListValue.add(index, generateGenericIntervalInfo())
        }
        _state.value = state.value.copy(
            intervalList = intervalListValue
        )
    }

    private fun onIntervalRemove(index: Int) {
        val intervalListValue = state.value.intervalList.toMutableList()
        if (intervalListValue.size == 1)
            return
        if (index in 0 until intervalListValue.size) {
            intervalListValue.removeAt(index)
            _state.value = state.value.copy(
                intervalList = intervalListValue
            )
        }

    }

    private fun onIntervalMove(up: Boolean, index: Int) {
        val intervalListValue = state.value.intervalList.toMutableList()

        // prevent out of bound movements
        if ((up && index == 0) || (!up && index == intervalListValue.lastIndex))
            return

        if (index in 0 until intervalListValue.size) {
            val otherIndex = if (up) index - 1 else index + 1
            intervalListValue.swap(index, otherIndex)
            _state.value = state.value.copy(
                intervalList = intervalListValue
            )
        }
    }

    private fun onIntervalEditForSoundEffect(index: Int, soundEffect: IntervalEndSound) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
            updateIntervalData(
                index, intervalEditingInfo.intervalData.copy(
                    soundEffect = soundEffect
                )
            )
        }

    private fun onIntervalEditForTimeIsIncreasing(index: Int, timeIsIncreasing: Boolean) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
            updateIntervalData(
                index, intervalEditingInfo.intervalData.copy(
                    increasing = timeIsIncreasing
                )
            )
        }

    private fun onIntervalEditForMinute(index: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
                updateTimeRepresentationPair(
                    index, intervalEditingInfo.timeRepresentationPair.copy(first = value)
                )

            }
        }

    private fun onIntervalEditForSecond(index: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
                updateTimeRepresentationPair(
                    index, intervalEditingInfo.timeRepresentationPair.copy(second = value)
                )

            }
        }

    private fun onIntervalEditForMaxScoreInput(index: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            updateMaxScoreInput(
                index, value
            )
        }

    private fun onIntervalEditForAllowDeuceAdv(index: Int, allow: Boolean) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
            val maxScore = getFilteredValue(intervalEditingInfo.maxScoreInput)?.toIntOrNull() ?: 0
            updateScoreInfo(
                index, intervalEditingInfo.scoreInfo.copy(
                    scoreRule = if (allow) ScoreRule.Trigger.DeuceAdvantage(maxScore) else ScoreRule.Trigger.Max(maxScore)
                )
            )
        }

    private fun onIntervalEditForTeamCount(index: Int, teamCount: Int) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
            val dataList = intervalEditingInfo.scoreInfo.dataList
            val newDataList = if (teamCount < dataList.size && teamCount > 0) {
                dataList.subList(0, teamCount)
            } else if (teamCount > dataList.size) {
                dataList + (0 until (teamCount - dataList.size)).map {
                    generateDefaultScoreGroup()
                }
            } else
                dataList
            updateScoreInfo(
                index, intervalEditingInfo.scoreInfo.copy(
                    dataList = newDataList
                )
            )
        }

    private fun onIntervalEditForInitialScoreInput(index: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            updateInitialScoreInput(
                index, value
            )
        }

    private fun onIntervalEditForPrimaryIncrementAdd(index: Int) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
            val newList = intervalEditingInfo.primaryIncrementInputList + "+1"
            updatePrimaryIncrementList(
                index, newList
            )
        }

    private fun onIntervalEditForPrimaryIncrement(index: Int, incrementIndex: Int, value: String) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
            val newList = intervalEditingInfo.primaryIncrementInputList.toMutableList()
            if (incrementIndex in 0 until newList.size)
                newList[incrementIndex] = value
            updatePrimaryIncrementList(
                index, newList
            )
        }

    private fun onIntervalEditForPrimaryIncrementMove(index: Int, incrementIndex: Int, up: Boolean) {
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->

            val newList = intervalEditingInfo.primaryIncrementInputList.toMutableList()

            // prevent out of bound movements
            if ((up && incrementIndex == 0) || (!up && incrementIndex == newList.lastIndex))
                return


            if (incrementIndex in 0 until newList.size) {
                val otherIndex = if (up) incrementIndex - 1 else incrementIndex + 1
                newList.swap(incrementIndex, otherIndex)
            }

            updatePrimaryIncrementList(
                index, newList
            )
        }
    }

    private fun onIntervalEditForPrimaryIncrementRemove(index: Int, incrementIndex: Int) {
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->

            val newList = intervalEditingInfo.primaryIncrementInputList.toMutableList()
            if (newList.size == 1)
                return
            if (incrementIndex in 0 until newList.size)
                newList.removeAt(incrementIndex)

            updatePrimaryIncrementList(
                index, newList
            )
        }
    }

    private fun onIntervalEditForPrimaryIncrementRefresh(index: Int, incrementIndex: Int) {
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->

            val newList = intervalEditingInfo.primaryIncrementInputList.toMutableList()
            if (incrementIndex in 0 until newList.size)
                newList[incrementIndex] = newList[incrementIndex].asIncrementDisplay()

            updatePrimaryIncrementList(
                index, newList
            )
        }
    }

    private fun onIntervalEditForPrimaryMappingAllowed(index: Int, allowed: Boolean) =
        updatePrimaryMappingAllowed(index, allowed)


    private fun onIntervalEditForPrimaryMappingAdd(index: Int) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->

            val max = intervalEditingInfo.primaryMappingInputList.maxOfOrNull { (key, _) -> key.toIntOrNull() ?: -1 } ?: -1

            val newList = intervalEditingInfo.primaryMappingInputList + ((max + 1).toString() to "")
            updatePrimaryMappingList(
                index, newList
            )
        }

    private fun onIntervalEditForPrimaryMappingOriginalScore(index: Int, mappingIndex: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->

                val newList = intervalEditingInfo.primaryMappingInputList.toMutableList()
                if (mappingIndex in 0 until newList.size)
                    newList[mappingIndex] = newList[mappingIndex].copy(first = value)

                updatePrimaryMappingList(
                    index, newList
                )
            }
        }

    private fun onIntervalEditForPrimaryMappingDisplayScore(index: Int, mappingIndex: Int, value: String) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->

            val newList = intervalEditingInfo.primaryMappingInputList.toMutableList()
            if (mappingIndex in 0 until newList.size)
                newList[mappingIndex] = newList[mappingIndex].copy(second = value)

            updatePrimaryMappingList(
                index, newList
            )
        }

    private fun onIntervalEditForPrimaryMappingMove(index: Int, mappingIndex: Int, up: Boolean) {
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->

            val newList = intervalEditingInfo.primaryMappingInputList.toMutableList()

            // prevent out of bound movements
            if ((up && mappingIndex == 0) || (!up && mappingIndex == newList.lastIndex))
                return


            if (mappingIndex in 0 until newList.size) {
                val otherIndex = if (up) mappingIndex - 1 else mappingIndex + 1
                newList.swap(mappingIndex, otherIndex)
            }

            updatePrimaryMappingList(
                index, newList
            )
        }
    }

    private fun onIntervalEditForPrimaryMappingRemove(index: Int, mappingIndex: Int) {
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->

            val newList = intervalEditingInfo.primaryMappingInputList.toMutableList()
            if (newList.size == 1)
                return
            if (mappingIndex in 0 until newList.size)
                newList.removeAt(mappingIndex)

            updatePrimaryMappingList(
                index, newList
            )
        }
    }

    private fun onIntervalEditForSecondaryScoreAllowed(index: Int, allowed: Boolean) =
        updateSecondaryScoreAllowed(index, allowed)

    private fun onIntervalEditForSecondaryScoreLabel(index: Int, value: String) =
        state.value.intervalList.getOrNull(index)?.also { intervalEditingInfo ->
            updateScoreInfo(
                index, intervalEditingInfo.scoreInfo.copy(
                    secondaryScoreLabel = value
                )
            )
        }

    private fun updateScoreInfo(index: Int, scoreInfo: ScoreInfo) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(scoreInfo = scoreInfo)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun updateIntervalData(index: Int, intervalData: IntervalData) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(intervalData = intervalData)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun updateTimeRepresentationPair(index: Int, timeRepresentationPair: Pair<String, String>) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(timeRepresentationPair = timeRepresentationPair)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun updateMaxScoreInput(index: Int, maxScore: String) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(maxScoreInput = maxScore)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun updateInitialScoreInput(index: Int, initialScore: String) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(initialScoreInput = initialScore)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun updatePrimaryIncrementList(index: Int, increments: List<String>) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(primaryIncrementInputList = increments)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun updatePrimaryMappingAllowed(index: Int, allowed: Boolean) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(allowPrimaryMapping = allowed)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun updatePrimaryMappingList(index: Int, mapping: List<Pair<String, String>>) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(primaryMappingInputList = mapping)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun updateSecondaryScoreAllowed(index: Int, allowed: Boolean) {
        val newList = state.value.intervalList.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(allowSecondaryScore = allowed)
        _state.value = state.value.copy(
            intervalList = newList
        )
    }

    private fun getFilteredValue(value: String) = if (value.isEmpty())
        ""
    else if (StringUtils.isNumeric(value))
        value
    else
        null
}
