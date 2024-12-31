package com.dgnt.quickScoreboardCreator.feature.history.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel

class HistoryMapperDataToDomain(
    private val historicalScoreboardMapperDataToDomain: Mapper<HistoricalScoreboardData, HistoricalScoreboard>
) : Mapper<HistoryEntity, HistoryModel> {


    override fun map(from: HistoryEntity) = HistoryModel(
        from.id,
        from.title,
        from.description,
        from.icon,
        from.lastModified,
        from.createdAt,
        historicalScoreboardMapperDataToDomain.map(from.historicalScoreboard),
        from.temporary,
    )

}

