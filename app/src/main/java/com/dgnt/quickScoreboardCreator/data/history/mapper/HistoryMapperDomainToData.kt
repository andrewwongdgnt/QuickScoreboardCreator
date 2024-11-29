package com.dgnt.quickScoreboardCreator.data.history.mapper

import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.mapper.Mapper
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity

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

