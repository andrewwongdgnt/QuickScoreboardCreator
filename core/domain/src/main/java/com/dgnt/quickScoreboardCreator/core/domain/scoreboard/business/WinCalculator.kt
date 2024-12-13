package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule


interface WinCalculator {
    fun store(scoreInfo: ScoreInfo, intervalIndex: Int)

    fun calculate(winRule: WinRule): Set<Int>
}