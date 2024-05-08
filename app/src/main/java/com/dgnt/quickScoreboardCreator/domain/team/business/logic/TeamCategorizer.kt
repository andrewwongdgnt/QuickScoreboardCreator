package com.dgnt.quickScoreboardCreator.domain.team.business.logic

import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.model.CategorizedTeamItemData

fun interface TeamCategorizer {

    operator fun invoke(teamEntityList: List<TeamEntity>): List<CategorizedTeamItemData>

}