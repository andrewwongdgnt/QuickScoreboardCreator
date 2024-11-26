package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class GetScoreboardListUseCase @Inject constructor(
    private val repository: ScoreboardRepository
) {

    operator fun invoke() =
        repository.getAll()

}