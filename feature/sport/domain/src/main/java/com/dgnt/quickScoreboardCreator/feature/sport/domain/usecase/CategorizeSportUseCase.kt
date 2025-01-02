package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.CategorizedSportListItem
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.CategorizedSportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportListItem
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import javax.inject.Inject

class CategorizeSportUseCase @Inject constructor() {
    operator fun invoke(sportTypeList: List<SportType>, sportItemList: List<SportModel>): Pair<CategorizedSportType, CategorizedSportListItem> {
        val sport = sportItemList.map { e ->
            SportListItem(
                e.sportIdentifier ?: SportIdentifier.Custom(-1), e.title, e.description, e.icon
            )
        }
        return CategorizedSportType(sportTypeList) to
                CategorizedSportListItem(sport.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, SportListItem::title)))

    }
}