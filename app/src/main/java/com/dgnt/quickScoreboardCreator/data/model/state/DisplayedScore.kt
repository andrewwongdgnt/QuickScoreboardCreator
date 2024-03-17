package com.dgnt.quickScoreboardCreator.data.model.state

sealed class DisplayedScore {
    class CustomDisplayedScore(val display: String): DisplayedScore()
    data object Deuce : DisplayedScore()
    data object Advantage : DisplayedScore()
    data object Blank : DisplayedScore()
}