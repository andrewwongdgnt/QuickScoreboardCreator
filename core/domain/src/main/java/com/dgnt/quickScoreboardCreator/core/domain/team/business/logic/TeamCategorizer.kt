package com.dgnt.quickScoreboardCreator.core.domain.team.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.team.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel


fun interface TeamCategorizer {

    operator fun invoke(teamEntityList: List<TeamModel>): List<CategorizedTeamItemData>

}