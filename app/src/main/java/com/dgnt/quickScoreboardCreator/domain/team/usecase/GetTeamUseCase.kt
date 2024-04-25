package com.dgnt.quickScoreboardCreator.domain.team.usecase

import com.dgnt.quickScoreboardCreator.domain.team.repository.TeamRepository

class GetTeamUseCase(
    private val repository: TeamRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

}