package com.dgnt.quickScoreboardCreator.feature.team.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.team.domain.repository.TeamRepository
import javax.inject.Inject

class GetTeamUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

}