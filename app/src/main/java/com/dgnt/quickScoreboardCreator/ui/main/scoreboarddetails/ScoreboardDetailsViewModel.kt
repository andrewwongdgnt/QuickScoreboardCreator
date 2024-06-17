package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.ID
import com.dgnt.quickScoreboardCreator.ui.common.Arguments.TYPE
import com.dgnt.quickScoreboardCreator.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ScoreboardDetailsViewModel @Inject constructor(
    private val resources: Resources,
    private val insertScoreboardListUseCase: InsertScoreboardListUseCase,
    private val getScoreboardUseCase: GetScoreboardUseCase,
    private val scoreboardLoader: ScoreboardLoader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var scoreboardId: Int? = null

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _icon = MutableStateFlow<ScoreboardIcon?>(null)
    val icon: StateFlow<ScoreboardIcon?> = _icon.asStateFlow()

    private val _iconChanging = MutableStateFlow(false)
    val iconChanging: StateFlow<Boolean> = _iconChanging.asStateFlow()

    private val _winRule = MutableStateFlow<WinRule>(WinRule.Count)
    val winRule = _winRule.asStateFlow()

    val valid: StateFlow<Boolean> = title.map {
        it.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(ID)?.takeUnless { it < 0 }?.let { id ->
            initWithId(id)
        } ?: savedStateHandle.get<ScoreboardType>(TYPE)?.let {
            initWithScoreboardType(it)
        }

    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getScoreboardUseCase(id)?.let {
                _title.value = it.title
                _description.value = it.description
                _icon.value = it.icon
                scoreboardId = it.id
            }
        }
    }

    private fun initWithScoreboardType(scoreboardType: ScoreboardType) {
        _title.value = resources.getString(scoreboardType.titleRes)
        _description.value = resources.getString(scoreboardType.descriptionRes)
        _icon.value = when (scoreboardType) {
            ScoreboardType.NONE -> ScoreboardIcon.entries.toTypedArray().let {
                it[Random.nextInt(it.size)]
            }

            else -> scoreboardType.icon
        }
        scoreboardType.rawRes?.let { rawRes ->
            scoreboardLoader(resources.openRawResource(rawRes)) as DefaultScoreboardConfig?
        }?.let {
            _winRule.value = it.winRuleType.toWinRule()
        }
    }

    fun onEvent(event: ScoreboardDetailsEvent) {
        when (event) {
            ScoreboardDetailsEvent.OnConfirm -> {
                if (valid.value) {
                    viewModelScope.launch {
                        insertScoreboardListUseCase(
                            listOf(
                                ScoreboardEntity(
                                    id = scoreboardId,
                                    title = title.value,
                                    description = description.value,
                                    icon = icon.value!!
                                )
                            )
                        )
                    }
                }
                sendUiEvent(UiEvent.Done)
            }

            ScoreboardDetailsEvent.OnDismiss -> sendUiEvent(UiEvent.Done)
            is ScoreboardDetailsEvent.OnDescriptionChange -> _description.value = event.descriptionChange
            is ScoreboardDetailsEvent.OnTitleChange -> _title.value = event.title
            is ScoreboardDetailsEvent.OnIconEdit -> _iconChanging.value = true
            is ScoreboardDetailsEvent.OnNewIcon -> {
                _icon.value = event.icon
                _iconChanging.value = false
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}