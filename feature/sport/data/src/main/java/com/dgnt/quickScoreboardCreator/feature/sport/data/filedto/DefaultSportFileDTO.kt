package com.dgnt.quickScoreboardCreator.feature.sport.data.filedto

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportType


data class DefaultSportFileDTO(

    val sportType: SportType

) : SportFileDTO()
