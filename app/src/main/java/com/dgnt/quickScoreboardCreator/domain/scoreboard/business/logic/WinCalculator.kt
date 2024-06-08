package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule

interface WinCalculator {
    fun store(scoreInfo: ScoreInfo, intervalIndex: Int)

    fun calculate(winRule: WinRule): Set<Int>
}