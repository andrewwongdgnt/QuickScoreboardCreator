package com.dgnt.quickScoreboardCreator.core.domain.team.usecase

import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
import javax.inject.Inject

class GetTeamUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

}