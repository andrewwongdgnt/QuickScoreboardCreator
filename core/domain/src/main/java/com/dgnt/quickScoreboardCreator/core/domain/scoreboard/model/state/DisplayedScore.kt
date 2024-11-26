package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.state

sealed interface DisplayedScore {
    data class Custom(val display: String) : DisplayedScore
    data object Deuce : DisplayedScore
    data object Advantage : DisplayedScore
    data object Blank : DisplayedScore
}