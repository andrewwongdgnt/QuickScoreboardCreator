package com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class GetScoreboardListUseCase @Inject constructor(
    private val repository: ScoreboardRepository
) {

    operator fun invoke() =
        repository.getAll()

}