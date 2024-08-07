package com.dgnt.quickScoreboardCreator.domain.team.usecase

import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.repository.TeamRepository
import javax.inject.Inject

class InsertTeamUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    suspend operator fun invoke(teamEntity: TeamEntity) =
        repository.insert(teamEntity)

}