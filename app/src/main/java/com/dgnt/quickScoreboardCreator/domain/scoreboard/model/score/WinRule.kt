package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score

sealed class WinRule(val scoreCarriesOver: Boolean) {
    data object Sum : WinRule(false)
    data object Final : WinRule(true)
    data object Count : WinRule(false)
}