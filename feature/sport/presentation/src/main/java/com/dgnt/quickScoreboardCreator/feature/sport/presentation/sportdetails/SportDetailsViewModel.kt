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
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreGroup
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

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _icon = MutableStateFlow<SportIcon?>(null)
    val icon: StateFlow<SportIcon?> = _icon.asStateFlow()

    private val _iconChanging = MutableStateFlow(false)
    val iconChanging: StateFlow<Boolean> = _iconChanging.asStateFlow()

    private val _intervalLabel = MutableStateFlow("")
    val intervalLabel = _intervalLabel.asStateFlow()

    private val _isNewEntity = MutableStateFlow(true)
    val isNewEntity = _isNewEntity.asStateFlow()

    private val _winRule = MutableStateFlow<WinRule>(WinRule.Final)
    val winRule = _winRule.asStateFlow()

    private val _intervalList = MutableStateFlow(
        listOf(
            generateGenericIntervalInfo()
        )
    )
    val intervalList = _intervalList.asStateFlow()

    val valid = combine(
        title,
        intervalLabel,
        intervalList
    ) { title, intervalLabel, intervalList ->

        title.isNotBlank() && intervalLabel.isNotBlank() && intervalList.all { interval ->

            val checkingTimeLimitAndMaxScore = (interval.intervalData.increasing && interval.maxScoreInput.toIntOrNull()?.let { it > 0 } == true) || (!interval.intervalData.increasing && (interval.timeRepresentationPair.first.toIntOrNull()?.let { it > 0 } == true || interval.timeRepresentationPair.second.toIntOrNull()?.let { it > 0 } == true))

            val checkingInitialScoreInput = (interval.initialScoreInput.isNotBlank())

            val checkingIncrementList = interval.primaryIncrementInputList.all { it.isNotBlank() }

            val checkingPrimaryMapping = (interval.allowPrimaryMapping && interval.primaryMappingInputList.isNotEmpty() && interval.primaryMappingInputList.all { it.first.isNotBlank() && it.second.isNotBlank() }) || !interval.allowPrimaryMapping

            val checkingSecondaryScoring = (interval.allowSecondaryScore && interval.scoreInfo.secondaryScoreLabel.isNotBlank()) || !interval.allowSecondaryScore

            checkingTimeLimitAndMaxScore && checkingInitialScoreInput && checkingIncrementList && checkingPrimaryMapping && checkingSecondaryScoring
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        savedStateHandle.get<SportIdentifier?>(SPORT_IDENTIFIER)?.let { sId ->
            when (sId) {
                is SportIdentifier.Custom -> initWithId(sId.id)
                is SportIdentifier.Default -> initWithSportType(sId.sportType)
            }
        } ?: run {
            _icon.value = SportIcon.entries.toTypedArray().let {
                it[Random.nextInt(it.size)]
            }
        }

    }

    private fun initWithId(id: Int) = viewModelScope.launch {
        originalModel = getSportUseCase(id)?.also {
            _title.value = it.title
            _description.value = it.description
            _winRule.value = it.winRule
            _icon.value = it.icon
            _intervalLabel.value = it.intervalLabel
            _isNewEntity.value = false
        }
    }

    private fun initWithSportType(sportType: SportType) {
        _title.value = resources.getString(sportType.titleRes())
        _description.value = resources.getString(sportType.descriptionRes())
        _intervalLabel.value = resources.getString(sportType.intervalLabelRes())
        _isNewEntity.value = true
        getSportUseCase(resources.openRawResource(sportType.rawRes()))?.let {
            _winRule.value = it.winRule
            _icon.value = it.icon

            _intervalList.value = it.intervalList.map { interval ->
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
        }
    }

    fun onConfirm() {
        if (valid.value) {
            viewModelScope.launch {
                insertSportUseCase(
                    SportModel(
                        sportIdentifier = originalModel?.sportIdentifier,
                        title = title.value,
                        description = description.value,
                        winRule = winRule.value,
                        icon = icon.value!!,
                        intervalLabel = intervalLabel.value,
                        intervalList = listOf()
                    )
                )
            }
        }
        sendUiEvent(Done)
    }

    fun onDismiss() = sendUiEvent(Done)

    fun onDelete() = viewModelScope.launch {

        originalModel?.let {
            deleteSportUseCase(it)
        }
        sendUiEvent(Done)
    }

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onWinRuleChange(winRule: WinRule) {
        _winRule.value = winRule
    }

    fun onIconEdit(changing: Boolean = true) {
        _iconChanging.value = changing
    }

    fun onIconChange(icon: SportIcon) {
        _icon.value = icon
        _iconChanging.value = false
    }

    fun onIntervalLabelChange(intervalLabel: String) {
        _intervalLabel.value = intervalLabel
    }

    fun onIntervalAdd(index: Int? = null) {
        val intervalListValue = intervalList.value.toMutableList()
        if (index == null) {
            intervalListValue.add(generateGenericIntervalInfo())
        } else if (index in 0..intervalListValue.size) {
            intervalListValue.add(index, generateGenericIntervalInfo())
        }
        _intervalList.value = intervalListValue

    }

    fun onIntervalRemove(index: Int) {
        val intervalListValue = intervalList.value.toMutableList()
        if (intervalListValue.size == 1)
            return
        if (index in 0 until intervalListValue.size) {
            intervalListValue.removeAt(index)
            _intervalList.value = intervalListValue
        }

    }

    fun onIntervalMove(up: Boolean, index: Int) {
        val intervalListValue = intervalList.value.toMutableList()

        // prevent out of bound movements
        if ((up && index == 0) || (!up && index == intervalListValue.lastIndex))
            return

        if (index in 0 until intervalListValue.size) {
            val otherIndex = if (up) index - 1 else index + 1
            intervalListValue.swap(index, otherIndex)
            _intervalList.value = intervalListValue
        }
    }

    fun onIntervalEditForSoundEffect(index: Int, soundEffect: IntervalEndSound) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
            updateIntervalData(
                index, intervalEditingInfo.intervalData.copy(
                    soundEffect = soundEffect
                )
            )
        }

    fun onIntervalEditForTimeIsIncreasing(index: Int, timeIsIncreasing: Boolean) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
            updateIntervalData(
                index, intervalEditingInfo.intervalData.copy(
                    increasing = timeIsIncreasing
                )
            )
        }

    fun onIntervalEditForMinute(index: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
                updateTimeRepresentationPair(
                    index, intervalEditingInfo.timeRepresentationPair.copy(first = value)
                )

            }
        }

    fun onIntervalEditForSecond(index: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
                updateTimeRepresentationPair(
                    index, intervalEditingInfo.timeRepresentationPair.copy(second = value)
                )

            }
        }

    fun onIntervalEditForMaxScoreInput(index: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            updateMaxScoreInput(
                index, value
            )
        }

    fun onIntervalEditForAllowDeuceAdv(index: Int, allow: Boolean) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
            val maxScore = getFilteredValue(intervalEditingInfo.maxScoreInput)?.toIntOrNull() ?: 0
            updateScoreInfo(
                index, intervalEditingInfo.scoreInfo.copy(
                    scoreRule = if (allow) ScoreRule.Trigger.DeuceAdvantage(maxScore) else ScoreRule.Trigger.Max(maxScore)
                )
            )
        }

    fun onIntervalEditForTeamCount(index: Int, teamCount: Int) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
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

    fun onIntervalEditForInitialScoreInput(index: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            updateInitialScoreInput(
                index, value
            )
        }

    fun onIntervalEditForPrimaryIncrementAdd(index: Int) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
            val newList = intervalEditingInfo.primaryIncrementInputList + "+1"
            updatePrimaryIncrementList(
                index, newList
            )
        }

    fun onIntervalEditForPrimaryIncrement(index: Int, incrementIndex: Int, value: String) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
            val newList = intervalEditingInfo.primaryIncrementInputList.toMutableList()
            if (incrementIndex in 0 until newList.size)
                newList[incrementIndex] = value
            updatePrimaryIncrementList(
                index, newList
            )
        }

    fun onIntervalEditForPrimaryIncrementMove(index: Int, incrementIndex: Int, up: Boolean) {
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->

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

    fun onIntervalEditForPrimaryIncrementRemove(index: Int, incrementIndex: Int) {
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->

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

    fun onIntervalEditForPrimaryIncrementRefresh(index: Int, incrementIndex: Int) {
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->

            val newList = intervalEditingInfo.primaryIncrementInputList.toMutableList()
            if (incrementIndex in 0 until newList.size)
                newList[incrementIndex] = newList[incrementIndex].asIncrementDisplay()

            updatePrimaryIncrementList(
                index, newList
            )
        }
    }

    fun onIntervalEditForPrimaryMappingAllowed(index: Int, allowed: Boolean) =
        updatePrimaryMappingAllowed(index, allowed)


    fun onIntervalEditForPrimaryMappingAdd(index: Int) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->

            val max = intervalEditingInfo.primaryMappingInputList.maxOfOrNull { (key, _) -> key.toIntOrNull() ?: -1 } ?: -1

            val newList = intervalEditingInfo.primaryMappingInputList + ((max + 1).toString() to "")
            updatePrimaryMappingList(
                index, newList
            )
        }

    fun onIntervalEditForPrimaryMappingOriginalScore(index: Int, mappingIndex: Int, rawValue: String) =
        getFilteredValue(rawValue)?.let { value ->
            intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->

                val newList = intervalEditingInfo.primaryMappingInputList.toMutableList()
                if (mappingIndex in 0 until newList.size)
                    newList[mappingIndex] = newList[mappingIndex].copy(first = value)

                updatePrimaryMappingList(
                    index, newList
                )
            }
        }

    fun onIntervalEditForPrimaryMappingDisplayScore(index: Int, mappingIndex: Int, value: String) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->

            val newList = intervalEditingInfo.primaryMappingInputList.toMutableList()
            if (mappingIndex in 0 until newList.size)
                newList[mappingIndex] = newList[mappingIndex].copy(second = value)

            updatePrimaryMappingList(
                index, newList
            )
        }

    fun onIntervalEditForPrimaryMappingMove(index: Int, mappingIndex: Int, up: Boolean) {
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->

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

    fun onIntervalEditForPrimaryMappingRemove(index: Int, mappingIndex: Int) {
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->

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

    fun onIntervalEditForSecondaryScoreAllowed(index: Int, allowed: Boolean) =
        updateSecondaryScoreAllowed(index, allowed)

    fun onIntervalEditForSecondaryScoreLabel(index: Int, value: String) =
        intervalList.value.getOrNull(index)?.also { intervalEditingInfo ->
            updateScoreInfo(
                index, intervalEditingInfo.scoreInfo.copy(
                    secondaryScoreLabel = value
                )
            )
        }

    private fun updateScoreInfo(index: Int, scoreInfo: ScoreInfo) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(scoreInfo = scoreInfo)
        _intervalList.value = newList
    }

    private fun updateIntervalData(index: Int, intervalData: IntervalData) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(intervalData = intervalData)
        _intervalList.value = newList
    }

    private fun updateTimeRepresentationPair(index: Int, timeRepresentationPair: Pair<String, String>) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(timeRepresentationPair = timeRepresentationPair)
        _intervalList.value = newList
    }

    private fun updateMaxScoreInput(index: Int, maxScore: String) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(maxScoreInput = maxScore)
        _intervalList.value = newList
    }

    private fun updateInitialScoreInput(index: Int, initialScore: String) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(initialScoreInput = initialScore)
        _intervalList.value = newList
    }

    private fun updatePrimaryIncrementList(index: Int, increments: List<String>) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(primaryIncrementInputList = increments)
        _intervalList.value = newList
    }

    private fun updatePrimaryMappingAllowed(index: Int, allowed: Boolean) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(allowPrimaryMapping = allowed)
        _intervalList.value = newList
    }

    private fun updatePrimaryMappingList(index: Int, mapping: List<Pair<String, String>>) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(primaryMappingInputList = mapping)
        _intervalList.value = newList
    }

    private fun updateSecondaryScoreAllowed(index: Int, allowed: Boolean) {
        val newList = intervalList.value.toMutableList()
        if (index in 0 until newList.size)
            newList[index] = newList[index].copy(allowSecondaryScore = allowed)
        _intervalList.value = newList
    }

    private fun generateGenericIntervalInfo() =
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

    private fun generateDefaultScoreGroup() = ScoreGroup(
        primary = ScoreData(
            current = 0,
            initial = 0,
            increments = listOf(1)
        ),
        secondary = null
    )

    private fun getFilteredValue(value: String) = if (value.isEmpty())
        ""
    else if (StringUtils.isNumeric(value))
        value
    else
        null
}
