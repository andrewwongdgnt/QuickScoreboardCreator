package com.dgnt.quickScoreboardCreator.feature.history.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel

class HistoryMapperDomainToData(
    private val historicalScoreboardMapperDomainToData: Mapper<HistoricalScoreboard, HistoricalScoreboardData>
) : Mapper<HistoryModel, HistoryEntity> {

    override fun map(from: HistoryModel) = HistoryEntity(
        from.id,
        from.title,
        from.description,
        from.icon,
        from.lastModified,
        from.createdAt,
        historicalScoreboardMapperDomainToData.map(from.historicalScoreboard),
        from.temporary,
    )

}

