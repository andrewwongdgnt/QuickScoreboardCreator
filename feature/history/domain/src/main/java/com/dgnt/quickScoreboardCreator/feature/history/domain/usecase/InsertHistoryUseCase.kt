package com.dgnt.quickScoreboardCreator.feature.history.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.repository.HistoryRepository
import javax.inject.Inject

class InsertHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(history: HistoryModel) =
        repository.insert(history)

}