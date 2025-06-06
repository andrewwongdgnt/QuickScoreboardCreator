package com.dgnt.quickScoreboardCreator.feature.history.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.repository.HistoryRepository
import javax.inject.Inject

class InsertHistoryListUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(historyList: List<HistoryModel>) =
        repository.insert(historyList)

}