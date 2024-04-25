package com.dgnt.quickScoreboardCreator.domain.team.usecase

import com.dgnt.quickScoreboardCreator.domain.team.repository.TeamRepository

class GetTeamListUseCase(
    private val repository: TeamRepository
) {

    operator fun invoke() =
        repository.getAll()

}