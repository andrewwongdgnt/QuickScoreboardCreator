package com.dgnt.quickScoreboardCreator.feature.sport.data.filedto

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule


enum class WinRuleType {
    SUM,
    FINAL,
    COUNT;

    fun toWinRule() =
        when (this) {
            SUM -> WinRule.Sum
            FINAL -> WinRule.Final
            COUNT -> WinRule.Count
        }

}