package com.dgnt.quickScoreboardCreator.domain.history.usecase

import com.dgnt.quickScoreboardCreator.domain.history.repository.HistoryRepository
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    operator fun invoke() =
        repository.getAll()

}