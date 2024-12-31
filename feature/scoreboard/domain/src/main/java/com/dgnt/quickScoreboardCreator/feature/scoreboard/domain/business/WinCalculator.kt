package com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule


interface WinCalculator {
    fun store(scoreInfo: ScoreInfo, intervalIndex: Int)

    fun calculate(winRule: WinRule): Set<Int>
}