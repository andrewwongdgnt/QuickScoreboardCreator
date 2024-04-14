package com.dgnt.quickScoreboardCreator.domain.model.state

data class DisplayedScoreInfo (
    val displayedScores: List<DisplayedScore>,
    val overallDisplayedScore: DisplayedScore
)