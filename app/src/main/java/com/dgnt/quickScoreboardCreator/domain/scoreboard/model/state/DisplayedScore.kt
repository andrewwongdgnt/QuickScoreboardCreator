package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.state

sealed class DisplayedScore {
    data class CustomDisplayedScore(val display: String) : DisplayedScore()
    data object Deuce : DisplayedScore()
    data object Advantage : DisplayedScore()
    data object Blank : DisplayedScore()
}