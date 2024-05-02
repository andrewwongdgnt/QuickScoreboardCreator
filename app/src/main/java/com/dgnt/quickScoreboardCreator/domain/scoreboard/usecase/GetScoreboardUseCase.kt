package com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class GetScoreboardUseCase @Inject constructor(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

}