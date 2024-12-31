package com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score

sealed class WinRule(val scoreCarriesOver: Boolean) {
    data object Sum : WinRule(false)
    data object Count : WinRule(false)
    data object Final : WinRule(true)
}