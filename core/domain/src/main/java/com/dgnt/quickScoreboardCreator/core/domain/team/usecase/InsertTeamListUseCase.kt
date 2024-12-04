package com.dgnt.quickScoreboardCreator.core.domain.team.usecase

import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
import javax.inject.Inject

class InsertTeamListUseCase @Inject constructor(
    private val repository: TeamRepository
) {

    suspend operator fun invoke(teamList: List<TeamModel>) =
        repository.insert(teamList)

}