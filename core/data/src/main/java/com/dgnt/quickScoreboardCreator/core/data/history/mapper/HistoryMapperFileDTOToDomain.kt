package com.dgnt.quickScoreboardCreator.core.data.history.mapper

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.history.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel

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

