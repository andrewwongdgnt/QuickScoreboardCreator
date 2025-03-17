package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard

import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.teamdisplay.TeamDisplay
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData

data class ScoreboardState(
    val primaryDisplayedScoreInfo: DisplayedScoreInfo = DisplayedScoreInfo(listOf(), DisplayedScore.Blank),
    val primaryIncrementList: List<List<Int>> = emptyList(),
    val secondaryDisplayedScoreInfo: DisplayedScoreInfo = DisplayedScoreInfo(listOf(), DisplayedScore.Blank),
    val secondaryIncrementList: List<List<Int>> = emptyList(),
    val secondaryScoreLabel: Label = Label.Custom(""),
    val teamList: List<TeamDisplay> = emptyList(),
    val timeData: TimeData = TimeData(0, 0, 0),
    val timerInProgress: Boolean = false,
    val simpleMode: Boolean = true,
    val intervalLabel: Label = Label.Custom(""),
    val currentInterval: Int = 1,
)
