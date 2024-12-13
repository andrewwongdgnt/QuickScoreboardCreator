package com.dgnt.quickScoreboardCreator.core.data.sport.filedto

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIcon

data class CustomSportFileDTO(

    val title: String,
    val description: String,
    val icon: SportIcon,
    val intervalLabel: String,

    ) : SportFileDTO()