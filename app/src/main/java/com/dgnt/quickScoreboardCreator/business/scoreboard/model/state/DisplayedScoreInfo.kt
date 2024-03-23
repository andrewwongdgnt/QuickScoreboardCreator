package com.dgnt.quickScoreboardCreator.business.scoreboard.model.state

data class DisplayedScoreInfo (
    val displayedScores: List<DisplayedScore>,
    val overallDisplayedScore: DisplayedScore
)