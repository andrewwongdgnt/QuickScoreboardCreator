package com.dgnt.quickScoreboardCreator.core.domain.sport.usecase

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.CategorizedSportListItem
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.CategorizedSportType
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportListItem
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportType

class QSCCategorizeSportUseCase : CategorizeSportUseCase {
    override fun invoke(sportTypeList: List<SportType>, sportItemList: List<SportModel>): Pair<CategorizedSportType, CategorizedSportListItem> {
        val sport = sportItemList.map { e ->
            SportListItem(
                e.sportIdentifier ?: SportIdentifier.Custom(-1), e.title, e.description, e.icon
            )
        }
        return CategorizedSportType(sportTypeList) to
                CategorizedSportListItem(sport.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, SportListItem::title)))

    }
}