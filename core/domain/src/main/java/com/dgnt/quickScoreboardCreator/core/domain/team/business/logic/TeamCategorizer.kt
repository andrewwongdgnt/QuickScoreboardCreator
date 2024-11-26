package com.dgnt.quickScoreboardCreator.core.domain.team.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.team.model.CategorizedTeamItemData


fun interface TeamCategorizer {

    operator fun invoke(teamEntityList: List<TeamEntity>): List<CategorizedTeamItemData>

}