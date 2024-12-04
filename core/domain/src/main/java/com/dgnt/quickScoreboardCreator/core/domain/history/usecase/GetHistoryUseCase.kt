package com.dgnt.quickScoreboardCreator.core.domain.history.usecase

import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

}