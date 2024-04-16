package com.dgnt.quickScoreboardCreator.domain.usecase

import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.repository.ScoreboardRepository
import kotlinx.coroutines.flow.Flow

class GetScoreboardListUseCase(
    private val repository: ScoreboardRepository
) {

    operator fun invoke(): Flow<List<ScoreboardEntity>> =
        repository.getAll()

}