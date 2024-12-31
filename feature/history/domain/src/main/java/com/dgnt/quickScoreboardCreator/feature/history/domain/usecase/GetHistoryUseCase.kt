package com.dgnt.quickScoreboardCreator.feature.history.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.history.domain.repository.HistoryRepository
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

}