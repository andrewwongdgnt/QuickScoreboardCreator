package com.dgnt.quickScoreboardCreator.core.domain.sport.usecase

import com.dgnt.quickScoreboardCreator.core.domain.sport.repository.SportRepository
import javax.inject.Inject

class GetSportListUseCase @Inject constructor(
    private val repository: SportRepository
) {

    operator fun invoke() =
        repository.getAll()

}