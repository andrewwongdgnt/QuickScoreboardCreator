package com.dgnt.quickScoreboardCreator.core.domain.sport.usecase

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.CategorizedSportListItem
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.CategorizedSportType
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportType

fun interface CategorizeSportUseCase {

    operator fun invoke(sportTypeList: List<SportType>, sportItemList: List<SportModel>): Pair<CategorizedSportType, CategorizedSportListItem>

}