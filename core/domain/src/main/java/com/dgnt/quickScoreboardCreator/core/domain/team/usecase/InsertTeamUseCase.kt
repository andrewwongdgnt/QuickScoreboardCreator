package com.dgnt.quickScoreboardCreator.core.domain.team.usecase

import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
import javax.inject.Inject

class InsertTeamUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    suspend operator fun invoke(team: TeamModel) =
        repository.insert(team)

}