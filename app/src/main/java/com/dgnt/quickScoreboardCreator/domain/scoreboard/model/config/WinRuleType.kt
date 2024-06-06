package com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule

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