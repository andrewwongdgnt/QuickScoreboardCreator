package com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository

class GetScoreboardListUseCase(
    private val repository: ScoreboardRepository
) {

    operator fun invoke() =
        repository.getAll()

}