package com.dgnt.quickScoreboardCreator.domain.usecase

import com.dgnt.quickScoreboardCreator.data.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.domain.repository.ScoreboardRepository

class InsertScoreboardListUseCase(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(scoreboardEntityList: List<ScoreboardEntity>) =
        repository.insert(scoreboardEntityList)

}