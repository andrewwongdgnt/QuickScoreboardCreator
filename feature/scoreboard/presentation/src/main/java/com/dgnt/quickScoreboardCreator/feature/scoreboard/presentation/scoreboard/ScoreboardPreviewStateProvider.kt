package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.Label
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScore
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state.DisplayedScoreInfo
import com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.scoreboard.teamdisplay.TeamDisplay
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.time.TimeData
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon

class ScoreboardPreviewStateProvider: CollectionPreviewParameterProvider<ScoreboardState>(
    listOf(
        ScoreboardState(
            primaryDisplayedScoreInfo = DisplayedScoreInfo(
                listOf(
                    DisplayedScore.Custom("10"),
                    DisplayedScore.Custom("21"),
                ),
                DisplayedScore.Blank
            ),
            primaryIncrementList = listOf(
                listOf(1, 2, 2),
                listOf(1, 2, 3),
            ),
            secondaryDisplayedScoreInfo = DisplayedScoreInfo(
                listOf(
                    DisplayedScore.Custom("1"),
                    DisplayedScore.Custom("0"),
                ),
                DisplayedScore.Blank
            ),
            secondaryIncrementList = listOf(
                listOf(1),
                listOf(1),
            ),
            secondaryScoreLabel = Label.Custom("S"),
            teamList = listOf(
                TeamDisplay.Selected("Gorillas Gorillas Gorillas Gorilla Gorillas Gorill", TeamIcon.GORILLA),
                TeamDisplay.Selected("Tigers Tigers Tigers Tigers Tigers", TeamIcon.TIGER)
            ),
            timeData = TimeData(12, 2, 4),
            timerInProgress = false,
            simpleMode = false,
            intervalLabel = Label.Custom("P"),
            currentInterval = 1,
        ),
        ScoreboardState(
            primaryDisplayedScoreInfo = DisplayedScoreInfo(
                listOf(
                    DisplayedScore.Custom("10"),
                    DisplayedScore.Custom("261"),
                ),
                DisplayedScore.Blank
            ),
            primaryIncrementList = listOf(
                listOf(1, 2, 23),
                listOf(1, 2, 3),
            ),
            secondaryDisplayedScoreInfo = DisplayedScoreInfo(
                listOf(
                    DisplayedScore.Custom("1"),
                    DisplayedScore.Custom("0"),
                ),
                DisplayedScore.Blank
            ),
            secondaryIncrementList = listOf(
                listOf(1),
                listOf(1),
            ),
            secondaryScoreLabel = Label.Resource(R.string.fouls),
            teamList = listOf(
                TeamDisplay.Selected("Gorillas", TeamIcon.GORILLA),
                TeamDisplay.Selected("Tigers", TeamIcon.TIGER)
            ),
            timeData = TimeData(12, 2, 4),
            timerInProgress = false,
            simpleMode = true,
            intervalLabel = Label.Resource(R.string.quarter),
            currentInterval = 1
        ),
        ScoreboardState(
            primaryDisplayedScoreInfo = DisplayedScoreInfo(
                listOf(
                    DisplayedScore.Advantage,
                    DisplayedScore.Blank,
                ),
                DisplayedScore.Blank
            ),
            primaryIncrementList = listOf(
                listOf(1, 2, 23),
                listOf(1, 2, 3),
            ),
            secondaryDisplayedScoreInfo = DisplayedScoreInfo(
                listOf(),
                DisplayedScore.Blank
            ),
            secondaryIncrementList = listOf(),
            secondaryScoreLabel = Label.Resource(R.string.blank),
            teamList = listOf(
                TeamDisplay.Selected("Gorillas", TeamIcon.GORILLA),
                TeamDisplay.Selected("Tigers", TeamIcon.TIGER)
            ),
            timeData = TimeData(12, 2, 4),
            timerInProgress = false,
            simpleMode = true,
            intervalLabel = Label.Resource(R.string.set),
            currentInterval = 2,
        ),
        ScoreboardState(
            primaryDisplayedScoreInfo = DisplayedScoreInfo(
                listOf(
                    DisplayedScore.Blank,
                    DisplayedScore.Blank,
                ),
                DisplayedScore.Deuce
            ),
            primaryIncrementList = listOf(
                listOf(1, 2, 23),
                listOf(1, 2, 3),
            ),
            secondaryDisplayedScoreInfo = DisplayedScoreInfo(
                listOf(),
                DisplayedScore.Blank
            ),
            secondaryIncrementList = listOf(),
            secondaryScoreLabel = Label.Resource(R.string.blank),
            teamList = listOf(
                TeamDisplay.Selected("Gorillas", TeamIcon.GORILLA),
                TeamDisplay.Selected("Tigers", TeamIcon.TIGER)
            ),
            timeData = TimeData(12, 2, 4),
            timerInProgress = false,
            simpleMode = false,
            intervalLabel = Label.Resource(R.string.game),
            currentInterval = 3,
        )
    )
)