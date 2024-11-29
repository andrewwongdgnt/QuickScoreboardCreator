package com.dgnt.quickScoreboardCreator.data.history.mapper

import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.mapper.Mapper
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoryEntity

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

