package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label
import com.dgnt.quickScoreboardCreator.core.presentation.ui.NavArguments.SPORT_IDENTIFIER
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.PlaySound
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TeamPicker
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.TimelineViewer
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.IntervalLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.TeamLabel
import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.InsertHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.manager.ScoreboardManager
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.teamdisplay.TeamDisplay
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.uievent.IntervalEditor
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.GetSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.TimeConversionUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.intervalLabelRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.rawRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.secondaryScoreLabelRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.soundEffectRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.titleRes
import com.dgnt.quickScoreboardCreator.feature.team.domain.usecase.GetTeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class ScoreboardViewModel @Inject constructor(
    private val resources: Resources,
    private val getSportUseCase: GetSportUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val insertHistoryUseCase: InsertHistoryUseCase,
    private val scoreboardManager: ScoreboardManager,
    private val timeConversionUseCase: TimeConversionUseCase,
    savedStateHandle: SavedStateHandle,
    private val uiEventHandler: UiEventHandler
) : ViewModel(), UiEventHandler by uiEventHandler {

    private val _state = MutableStateFlow(ScoreboardState())
    val state: StateFlow<ScoreboardState> = _state.asStateFlow()

    private val primaryScoresUpdateListener: (DisplayedScoreInfo) -> Unit = {
        _state.value = state.value.copy(
            primaryDisplayedScoreInfo = it
        )
    }

    private val secondaryScoresUpdateListener: (DisplayedScoreInfo) -> Unit = {
        _state.value = state.value.copy(
            secondaryDisplayedScoreInfo = it
        )
    }

    private val primaryIncrementListUpdateListener: (List<List<Int>>) -> Unit = {
        _state.value = state.value.copy(
            primaryIncrementList = it
        )
    }

    private val secondaryIncrementListUpdateListener: (List<List<Int>>) -> Unit = {
        _state.value = state.value.copy(
            secondaryIncrementList = it
        )
    }

    private val intervalIndexUpdateListener: (Int) -> Unit = {
        _state.value = state.value.copy(
            currentInterval = it + 1,
        )
        secondaryScoreLabelList.getOrNull(it)?.let { secondaryScoreLabelInfo ->
            _state.value = state.value.copy(
                secondaryScoreLabel = secondaryScoreLabelInfo,
            )
        }
        onTimerPause(true)
    }


    private val teamSizeUpdateListener: (Int) -> Unit = {
        _state.value = state.value.copy(
            teamList = (0 until it).map { index ->
                state.value.teamList.getOrNull(index) ?: TeamDisplay.UnSelected
            },
        )
    }

    private val timeUpdateListener: (Long) -> Unit = {
        _state.value = state.value.copy(
            timeData = timeConversionUseCase.toTimeData(it)
        )
        if (it <= 0L) {
            stopTimerJob()
        }
    }

    private var sportType: SportType? = null

    /**
     * Winners by team index
     */
    private val winnersUpdateListener: (Set<Int>) -> Unit = {
        //TODO handle winners

        stopTimerJob()
        isHistoryTemporary = false
        viewModelScope.launch {
            historyEntityId = insertHistory()
        }
    }

    private val intervalOnEndListener: (IntervalEndSound) -> Unit = { sound ->
        sound.soundEffectRes()?.let {
            sendUiEvent(PlaySound(it))
        }
    }

    private var timerJob: Job? = null

    private var sportIdentifier: SportIdentifier? = null

    private var timelineViewerTitle = ""
    private var timelineViewerIcon = SportIcon.BASKETBALL

    private var historyEntityId: Int? = null
    private var isHistoryTemporary = true

    private var secondaryScoreLabelList = listOf<Label>()

    init {
        savedStateHandle.get<SportIdentifier>(SPORT_IDENTIFIER)?.let { sId ->
            when (sId) {
                is SportIdentifier.Custom -> initWithId(sId.id)
                is SportIdentifier.Default -> initWithSportType(sId.sportType)
            }
            sportIdentifier = sId
        }

        scoreboardManager.let {
            it.primaryScoresUpdateListener = primaryScoresUpdateListener
            it.secondaryScoresUpdateListener = secondaryScoresUpdateListener
            it.timeUpdateListener = timeUpdateListener
            it.intervalIndexUpdateListener = intervalIndexUpdateListener
            it.primaryIncrementListUpdateListener = primaryIncrementListUpdateListener
            it.secondaryIncrementListUpdateListener = secondaryIncrementListUpdateListener
            it.teamSizeUpdateListener = teamSizeUpdateListener
            it.winnersUpdateListener = winnersUpdateListener
            it.intervalOnEndListener = intervalOnEndListener
            it.triggerUpdateListeners()
        }

    }

    private fun initWithId(id: Int) {
        viewModelScope.launch {
            getSportUseCase(id)?.let {
                timelineViewerTitle = it.title
                timelineViewerIcon = it.icon
            }
        }
    }

    private fun initWithSportType(sportType: SportType) {
        this.sportType = sportType
        _state.value = state.value.copy(
            intervalLabel = Label.Resource(sportType.intervalLabelRes())
        )
        timelineViewerTitle = resources.getString(sportType.titleRes())
        timelineViewerIcon = sportType.icon
        getSportUseCase(resources.openRawResource(sportType.rawRes()))?.let { sportModel ->
            scoreboardManager.apply {
                winRule = sportModel.winRule
                intervalList = sportModel.intervalList
            }
            secondaryScoreLabelList = sportModel.intervalList.map {
                Label.Resource(sportType.secondaryScoreLabelRes())
            }
        }

    }

    fun onAction(action: ScoreboardAction) {
        when (action) {
            is ScoreboardAction.IntervalEdit -> onIntervalEdit(action.updatedIntervalData)
            is ScoreboardAction.ScoreChange -> onScoreChange(action.isPrimary, action.scoreIndex, action.incrementIndex, action.main)
            is ScoreboardAction.TeamPick -> onTeamPick(action.updatedTeamData)
            is ScoreboardAction.TimerPause -> onTimerPause(action.reset)
            ScoreboardAction.TimerStart -> onTimerStart()
            ScoreboardAction.ToIntervalEditor -> toIntervalEditor()
            is ScoreboardAction.ToTeamPicker -> toTeamPicker(action.index)
            ScoreboardAction.ToTimelineViewer -> toTimelineViewer()
            is ScoreboardAction.ToggleModeChange -> onToggleModeChange(action.isSimpleMode)
        }
    }

    private fun onScoreChange(
        isPrimary: Boolean,
        scoreIndex: Int,
        incrementIndex: Int,
        main: Boolean,
    ) = scoreboardManager.updateScore(isPrimary, scoreIndex, incrementIndex, main)

    private fun toTeamPicker(index: Int) = sendUiEvent(TeamPicker(index))

    private fun onTeamPick(updatedTeamData: UpdatedTeamData) = viewModelScope.launch {
        _state.value = state.value.copy(
            teamList = (0 until scoreboardManager.currentTeamSize).mapNotNull { index ->
                if (updatedTeamData.scoreIndex == index) {
                    getTeamUseCase(updatedTeamData.teamId)?.let {
                        TeamDisplay.Selected(it.title, it.icon)
                    }
                } else {
                    state.value.teamList.getOrNull(index)
                }
            }
        )
    }

    private fun onTimerPause(reset: Boolean) {
        stopTimerJob()
        if (reset) {
            scoreboardManager.resetTime()
        }
    }

    private fun onTimerStart() {
        stopTimerJob()
        if (!scoreboardManager.canTimeAdvance()) {
            _state.value = state.value.copy(
                timerInProgress = false
            )
            return
        }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(100)
                scoreboardManager.updateTimeBy(100L)

            }
        }
        _state.value = state.value.copy(
            timerInProgress = true
        )
    }

    private fun onToggleModeChange(isSimpleMode: Boolean) {
        _state.value = state.value.copy(
            simpleMode = !isSimpleMode
        )
    }

    private fun toIntervalEditor() {
        stopTimerJob()
        state.value.run {
            sportIdentifier?.let { sId ->
                sendUiEvent(IntervalEditor(timeConversionUseCase.fromTimeData(timeData), currentInterval - 1, sId))
            }
        }
    }

    private fun onIntervalEdit(updatedIntervalData: UpdatedIntervalData) {
        updatedIntervalData.takeUnless { it.timeValue < 0 || it.intervalIndex < 0 }?.let {
            stopTimerJob()
            scoreboardManager.updateInterval(it.intervalIndex)
            scoreboardManager.updateTime(it.timeValue)

        }
    }

    private fun toTimelineViewer() = viewModelScope.launch {
        insertHistory().let {
            historyEntityId = it
            sendUiEvent(TimelineViewer(it, state.value.currentInterval - 1))
        }
    }

    private suspend fun insertHistory(): Int {
        val intervalLabel = when (val l = state.value.intervalLabel) {
            is Label.Custom -> IntervalLabel.Custom(l.value)
            else -> sportType?.let {
                IntervalLabel.DefaultSport(it)
            } ?: IntervalLabel.Custom("")
        }
        val teamList = state.value.teamList.map {
            when (it) {
                is TeamDisplay.UnSelected -> TeamLabel.None
                is TeamDisplay.Selected -> TeamLabel.Custom(it.name, it.icon)
            }
        }
        val historicalScoreboard = scoreboardManager.createTimeline(intervalLabel, teamList)

        return insertHistoryUseCase(
            HistoryModel(
                id = historyEntityId,
                title = timelineViewerTitle,
                description = "",
                icon = timelineViewerIcon,
                lastModified = DateTime.now(),
                historicalScoreboard = historicalScoreboard,
                temporary = isHistoryTemporary
            )
        ).toInt()
    }

    override fun onCleared() {
        super.onCleared()
        stopTimerJob()
    }

    private fun stopTimerJob() {
        timerJob?.cancel()
        _state.value = state.value.copy(
            timerInProgress = false
        )
    }
}