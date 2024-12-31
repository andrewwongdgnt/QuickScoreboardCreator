package com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel


fun interface TeamCategorizer {

    operator fun invoke(teamEntityList: List<TeamModel>): List<CategorizedTeamItemData>

}