package com.dgnt.quickScoreboardCreator.feature.history.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.history.domain.repository.HistoryRepository
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