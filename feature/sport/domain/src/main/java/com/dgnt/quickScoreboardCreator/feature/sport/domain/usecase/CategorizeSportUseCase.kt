package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportListItem
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType
import javax.inject.Inject

class CategorizeSportUseCase @Inject constructor() {
    operator fun invoke(sportTypeList: List<SportType>, sportItemList: List<SportModel>): Pair<List<SportType>, List<SportListItem>> {
        val sport = sportItemList.map { e ->
            SportListItem(
                e.sportIdentifier ?: SportIdentifier.Custom(-1), e.title, e.description, e.icon
            )
        }
        return sportTypeList to
                sport.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, SportListItem::title))

    }
}