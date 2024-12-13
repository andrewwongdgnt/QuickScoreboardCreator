package com.dgnt.quickScoreboardCreator.core.data.sport.filedto

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule


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