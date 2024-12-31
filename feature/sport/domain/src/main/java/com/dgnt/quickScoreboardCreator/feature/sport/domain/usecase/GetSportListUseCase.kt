package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.repository.SportRepository
import javax.inject.Inject

class GetSportListUseCase @Inject constructor(
    private val repository: SportRepository
) {

    operator fun invoke() =
        repository.getAll()

}