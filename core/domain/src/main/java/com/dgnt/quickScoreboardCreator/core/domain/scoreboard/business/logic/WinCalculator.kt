package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.WinRule

interface WinCalculator {
    fun store(scoreInfo: ScoreInfo, intervalIndex: Int)

    fun calculate(winRule: WinRule): Set<Int>
}