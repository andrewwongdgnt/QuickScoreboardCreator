package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.CategorizedScoreboardItemData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.CategorizedScoreboardType
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIdentifier
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardItemData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardType

class QSCScoreboardCategorizer : ScoreboardCategorizer {
    override fun invoke(scoreboardTypeList: List<ScoreboardType>, scoreboardList: List<ScoreboardModel>): Pair<CategorizedScoreboardType, CategorizedScoreboardItemData> {
        val scoreboardItemList = scoreboardList.map { e ->
            ScoreboardItemData(
                e.scoreboardIdentifier ?: ScoreboardIdentifier.Custom(-1), e.title, e.description, e.icon
            )
        }
        return CategorizedScoreboardType(scoreboardTypeList) to
                CategorizedScoreboardItemData(scoreboardItemList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, ScoreboardItemData::title)))

    }
}