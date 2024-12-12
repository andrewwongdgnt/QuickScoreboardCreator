package com.dgnt.quickScoreboardCreator.core.data.scoreboard.config

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.WinRule


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