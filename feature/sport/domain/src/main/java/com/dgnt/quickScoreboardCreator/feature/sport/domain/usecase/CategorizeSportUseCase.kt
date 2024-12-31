package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.CategorizedSportListItem
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.CategorizedSportType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType

fun interface CategorizeSportUseCase {

    operator fun invoke(sportTypeList: List<SportType>, sportItemList: List<SportModel>): Pair<CategorizedSportType, CategorizedSportListItem>

}