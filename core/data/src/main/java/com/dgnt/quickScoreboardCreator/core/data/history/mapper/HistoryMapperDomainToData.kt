package com.dgnt.quickScoreboardCreator.core.data.history.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel

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

