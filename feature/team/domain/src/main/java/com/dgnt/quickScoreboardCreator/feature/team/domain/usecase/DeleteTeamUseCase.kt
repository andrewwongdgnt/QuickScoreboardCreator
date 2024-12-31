package com.dgnt.quickScoreboardCreator.feature.team.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.repository.TeamRepository
import javax.inject.Inject

class DeleteTeamUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    suspend operator fun invoke(team: TeamModel) =
        repository.delete(team)

}