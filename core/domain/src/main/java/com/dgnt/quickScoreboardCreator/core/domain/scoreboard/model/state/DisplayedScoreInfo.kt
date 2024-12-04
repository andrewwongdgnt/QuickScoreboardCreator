package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.state

data class DisplayedScoreInfo (
    val displayedScores: List<DisplayedScore>,
    val overallDisplayedScore: DisplayedScore
)