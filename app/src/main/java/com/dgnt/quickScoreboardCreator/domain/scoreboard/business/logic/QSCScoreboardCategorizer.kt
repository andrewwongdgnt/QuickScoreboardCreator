package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic

import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardItemData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIdentifier

class QSCScoreboardCategorizer : ScoreboardCategorizer {
    override fun invoke(scoreboardTypeList: List<ScoreboardType>, scoreboardEntityList: List<ScoreboardEntity>): Pair<CategorizedScoreboardType, CategorizedScoreboardItemData> {
        val scoreboardList = scoreboardEntityList.map { e ->
            ScoreboardItemData(
                ScoreboardIdentifier.Custom(e.id ?: -1), e.title, e.description, e.icon
            )
        }
        return CategorizedScoreboardType(scoreboardTypeList) to
                CategorizedScoreboardItemData(scoreboardList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, ScoreboardItemData::title)))

    }
}