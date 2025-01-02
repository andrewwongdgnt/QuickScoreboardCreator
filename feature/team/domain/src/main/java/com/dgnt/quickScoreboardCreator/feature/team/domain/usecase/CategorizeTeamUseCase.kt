package com.dgnt.quickScoreboardCreator.feature.team.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.team.domain.model.CategorizedTeamItemData
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamItemData
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import javax.inject.Inject

class CategorizeTeamUseCase @Inject constructor() {
    operator fun invoke(teamEntityList: List<TeamModel>) =
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