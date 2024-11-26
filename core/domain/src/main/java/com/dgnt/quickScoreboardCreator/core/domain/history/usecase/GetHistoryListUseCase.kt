package com.dgnt.quickScoreboardCreator.core.domain.history.usecase

import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHistoryListUseCase @Inject constructor(
    private val repository: HistoryRepository
) {

    operator fun invoke() =
        repository.getAll().map {
            it.filterNot { e -> e.temporary }
        }

}