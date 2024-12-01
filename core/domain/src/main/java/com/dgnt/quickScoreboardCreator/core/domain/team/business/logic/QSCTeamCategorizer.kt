package com.dgnt.quickScoreboardCreator.core.domain.team.business.logic

import com.dgnt.quickScoreboardCreator.core.domain.team.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamItemData
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel

class QSCTeamCategorizer : TeamCategorizer {
    override fun invoke(teamEntityList: List<TeamModel>) =
        teamEntityList.mapNotNull { e ->
            e.id?.let { id ->
                TeamItemData(
                    id, e.title, e.description, e.icon
                )
            }
        }.let { teamList ->
            teamList.groupBy {
                it.title.first().uppercase()
            }
                .toSortedMap()
                .map {
                    CategorizedTeamItemData(it.key, it.value.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, TeamItemData::title)))
                }
        }


}