package com.dgnt.quickScoreboardCreator.domain.team.usecase

import com.dgnt.quickScoreboardCreator.domain.team.repository.TeamRepository
import javax.inject.Inject

class GetTeamListUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    operator fun invoke() =
        repository.getAll()

}