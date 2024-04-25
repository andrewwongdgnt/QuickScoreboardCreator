package com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository

class DeleteScoreboardListUseCase(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(scoreboardEntityList: List<ScoreboardEntity>) =
        repository.delete(scoreboardEntityList)

}