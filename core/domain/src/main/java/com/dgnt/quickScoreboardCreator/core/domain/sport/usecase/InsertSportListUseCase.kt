package com.dgnt.quickScoreboardCreator.core.domain.sport.usecase

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.repository.SportRepository
import javax.inject.Inject

class InsertSportListUseCase @Inject constructor(
    private val repository: SportRepository
) {

    suspend operator fun invoke(sportList: List<SportModel>) =
        repository.insert(sportList)

}