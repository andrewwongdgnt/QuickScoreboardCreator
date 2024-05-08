package com.dgnt.quickScoreboardCreator.domain.team.business.logic

import com.dgnt.quickScoreboardCreator.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.domain.team.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamItemData

class QSCTeamCategorizer : TeamCategorizer {
    override fun invoke(teamEntityList: List<TeamEntity>) =
        teamEntityList.mapNotNull { e ->
            e.id?.let { id ->
                TeamItemData(
                    id, e.title, e.description, e.teamIcon
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