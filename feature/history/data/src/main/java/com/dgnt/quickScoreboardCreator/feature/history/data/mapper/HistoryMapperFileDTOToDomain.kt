package com.dgnt.quickScoreboardCreator.feature.history.data.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel

class HistoryMapperFileDTOToDomain : Mapper<HistoryFileDTO, HistoryModel> {


    override fun map(from: HistoryFileDTO) = HistoryModel(
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

