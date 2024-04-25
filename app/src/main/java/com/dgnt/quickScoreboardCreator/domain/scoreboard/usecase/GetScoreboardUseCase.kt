package com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository

class GetScoreboardUseCase(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

}