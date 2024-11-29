package com.dgnt.quickScoreboardCreator.core.domain.history.usecase

import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository
import javax.inject.Inject

class DeleteHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(history: HistoryModel) =
        repository.delete(history)

}