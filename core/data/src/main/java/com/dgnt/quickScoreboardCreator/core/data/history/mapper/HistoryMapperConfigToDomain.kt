package com.dgnt.quickScoreboardCreator.core.data.history.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.history.config.HistoryConfig
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel

class HistoryMapperConfigToDomain : Mapper<HistoryConfig, HistoryModel> {


    override fun map(from: HistoryConfig) = HistoryModel(
        null,
        from.title,
        from.description,
        from.icon,
        from.lastModified,
        from.createdAt,
        HistoricalScoreboard(mapOf()), //TODO placeholder
        false
    )


}

