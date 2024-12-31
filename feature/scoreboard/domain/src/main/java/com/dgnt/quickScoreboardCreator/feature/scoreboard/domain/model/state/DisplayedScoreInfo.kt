package com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.model.state

data class DisplayedScoreInfo (
    val displayedScores: List<DisplayedScore>,
    val overallDisplayedScore: DisplayedScore
)