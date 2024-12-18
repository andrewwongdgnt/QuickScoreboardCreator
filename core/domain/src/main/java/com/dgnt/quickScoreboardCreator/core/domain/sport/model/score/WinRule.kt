package com.dgnt.quickScoreboardCreator.core.domain.sport.model.score

sealed class WinRule(val scoreCarriesOver: Boolean) {
    data object Sum : WinRule(false)
    data object Count : WinRule(false)
    data object Final : WinRule(true)
}