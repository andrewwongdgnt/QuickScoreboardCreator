package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic

import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType

class QSCScoreboardCategorizer : ScoreboardCategorizer {
    override fun invoke(scoreboardTypeList: List<ScoreboardType>, scoreboardEntityList: List<ScoreboardEntity>): Pair<CategorizedScoreboardType, CategorizedScoreboardItemData> {
        val scoreboardList = scoreboardEntityList.map { e ->
            ScoreboardItemData(
                e.id ?: -1, ScoreboardType.NONE, e.title, e.description
            )
        }
        return CategorizedScoreboardType(scoreboardTypeList) to
                CategorizedScoreboardItemData(scoreboardList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, ScoreboardItemData::title)))

    }
}