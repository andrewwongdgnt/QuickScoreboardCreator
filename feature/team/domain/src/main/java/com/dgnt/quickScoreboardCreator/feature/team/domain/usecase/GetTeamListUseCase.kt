package com.dgnt.quickScoreboardCreator.feature.team.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.team.domain.repository.TeamRepository
import javax.inject.Inject

class GetTeamListUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    operator fun invoke() =
        repository.getAll()

}