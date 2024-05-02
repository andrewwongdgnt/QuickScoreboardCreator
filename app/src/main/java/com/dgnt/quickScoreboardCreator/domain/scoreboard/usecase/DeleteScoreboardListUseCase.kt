package com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class DeleteScoreboardListUseCase @Inject constructor(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(scoreboardEntityList: List<ScoreboardEntity>) =
        repository.delete(scoreboardEntityList)

}