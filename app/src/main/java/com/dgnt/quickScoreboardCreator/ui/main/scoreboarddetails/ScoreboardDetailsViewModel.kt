package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.swap
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.DeleteScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.SCOREBOARD_IDENTIFIER
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ScoreboardDetailsViewModel @Inject constructor(
    private val resources: Resources,
    private val insertScoreboardUseCase: InsertScoreboardUseCase,
    private val getScoreboardUseCase: GetScoreboardUseCase,
    private val deleteScoreboardUseCase: DeleteScoreboardUseCase,
    private val scoreboardLoader: ScoreboardLoader,
    savedStateHandle: SavedStateHandle,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {

    private var originalEntity: ScoreboardEntity? = null

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _icon = MutableStateFlow<ScoreboardIcon?>(null)
    val icon: StateFlow<ScoreboardIcon?> = _icon.asStateFlow()

    private val _iconChanging = MutableStateFlow(false)
    val iconChanging: StateFlow<Boolean> = _iconChanging.asStateFlow()

    private val _isNewEntity = MutableStateFlow(true)
    val isNewEntity = _isNewEntity.asStateFlow()

    private val _winRule = MutableStateFlow<WinRule>(WinRule.Final)
    val winRule = _winRule.asStateFlow()

    private val _intervalList = MutableStateFlow(
        listOf(
            generateGenericIntervalInfo()
        )
    )

    private fun generateGenericIntervalInfo() =
        ScoreInfo(
            scoreRule = ScoreRule.None,
            scoreToDisplayScoreMap = mapOf(),
            dataList = listOf()
        ) to
                IntervalData(
                    current = 0,
                    initial = 0,
                    increasing = false
                )


    val intervalList = _intervalList.asStateFlow()

    val valid: StateFlow<Boolean> = title.map {
        it.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        savedStateHandle.get<ScoreboardIdentifier?>(SCOREBOARD_IDENTIFIER)?.let { sId ->
            when (sId) {
                is ScoreboardIdentifier.Custom -> initWithId(sId.id)
                is ScoreboardIdentifier.Default -> initWithScoreboardType(sId.scoreboardType)
            }
        } ?: run {
            _icon.value = ScoreboardIcon.entries.toTypedArray().let {
                it[Random.nextInt(it.size)]
            }
        }

    }

    private fun initWithId(id: Int) = viewModelScope.launch {
        originalEntity = getScoreboardUseCase(id)?.also {
            _title.value = it.title
            _description.value = it.description
            _winRule.value = it.winRule
            _icon.value = it.icon
            _isNewEntity.value = false
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {
        _title.value = resources.getString(scoreboardType.titleRes)
        _description.value = resources.getString(scoreboardType.descriptionRes)
        _icon.value = scoreboardType.icon
        _isNewEntity.value = true
        scoreboardType.rawRes.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let {
            _winRule.value = it.winRuleType.toWinRule()
        }
    }

    fun onConfirm() {
        if (valid.value) {
            viewModelScope.launch {
                insertScoreboardUseCase(
                    ScoreboardEntity(
                        id = originalEntity?.id,
                        title = title.value,
                        description = description.value,
                        winRule = winRule.value,
                        icon = icon.value!!
                    )
                )
            }
        }
        sendUiEvent(UiEvent.Done)
    }

    fun onDismiss() = sendUiEvent(UiEvent.Done)

    fun onDelete() = viewModelScope.launch {

        originalEntity?.let {
            deleteScoreboardUseCase(it)
        }
        sendUiEvent(UiEvent.Done)
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

    fun onIconChange(icon: ScoreboardIcon) {
        _icon.value = icon
        _iconChanging.value = false
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
        if (index == 0)
            return

        val intervalListValue = intervalList.value.toMutableList()
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

    fun onIntervalEdit(index: Int) {
        //TODO add code here
    }
}
