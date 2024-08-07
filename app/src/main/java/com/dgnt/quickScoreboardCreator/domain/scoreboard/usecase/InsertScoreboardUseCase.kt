package com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class InsertScoreboardUseCase @Inject constructor(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(scoreboardEntity: ScoreboardEntity) =
        repository.insert(scoreboardEntity)

}