package com.dgnt.quickScoreboardCreator.core.domain.history.usecase

import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository
import javax.inject.Inject

class InsertHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(history: HistoryModel) =
        repository.insert(history)

}