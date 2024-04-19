package com.dgnt.quickScoreboardCreator.domain.usecase

import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.repository.ScoreboardRepository
import kotlinx.coroutines.flow.Flow

class GetScoreboardUseCase(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

}