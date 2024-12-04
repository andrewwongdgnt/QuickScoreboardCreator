package com.dgnt.quickScoreboardCreator.core.domain.team.usecase

import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
import javax.inject.Inject

class GetTeamListUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    operator fun invoke() =
        repository.getAll()

}