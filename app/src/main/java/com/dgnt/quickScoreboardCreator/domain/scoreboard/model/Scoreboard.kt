package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule

sealed interface Scoreboard {
    val winRule: WinRule
    val intervalList: List<Pair<ScoreInfo, IntervalData>>
    data class DefaultScoreboard(
        val scoreboardType: ScoreboardType,
        override val winRule: WinRule,
        override val intervalList: List<Pair<ScoreInfo, IntervalData>>
    ):Scoreboard
}