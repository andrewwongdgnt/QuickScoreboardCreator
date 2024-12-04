package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class DeleteScoreboardUseCase @Inject constructor(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(scoreboard: ScoreboardModel) =
        repository.delete(scoreboard)

}