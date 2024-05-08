package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic

import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType

fun interface ScoreboardCategorizer {

    operator fun invoke(scoreboardTypeList: List<ScoreboardType>, scoreboardEntityList: List<ScoreboardEntity>): Pair<CategorizedScoreboardType, CategorizedScoreboardItemData>

}