package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardType

fun interface ScoreboardCategorizer {

    operator fun invoke(scoreboardTypeList: List<ScoreboardType>, scoreboardList: List<ScoreboardModel>): Pair<CategorizedScoreboardType, CategorizedScoreboardItemData>

}