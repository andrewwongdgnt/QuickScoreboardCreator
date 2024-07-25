package com.dgnt.quickScoreboardCreator.domain.history.usecase

import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.domain.history.repository.HistoryRepository
import javax.inject.Inject

class InsertHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(historyEntity: HistoryEntity) =
        repository.insert(historyEntity)

}