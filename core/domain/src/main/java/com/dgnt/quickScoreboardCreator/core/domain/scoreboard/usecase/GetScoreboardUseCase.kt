package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.usecase

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.repository.ScoreboardRepository
import java.io.InputStream
import javax.inject.Inject

class GetScoreboardUseCase @Inject constructor(
    private val repository: ScoreboardRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

    operator fun invoke(inputStream: InputStream) =
        repository.import(inputStream)

}