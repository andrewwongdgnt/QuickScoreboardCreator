package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.repository.ScoreboardRepository
import javax.inject.Inject

class InsertScoreboardListUseCase @Inject constructor(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(scoreboardList: List<ScoreboardModel>) =
        repository.insert(scoreboardList)

}