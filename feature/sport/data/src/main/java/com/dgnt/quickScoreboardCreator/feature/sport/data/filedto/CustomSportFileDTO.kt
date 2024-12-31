package com.dgnt.quickScoreboardCreator.feature.sport.data.filedto

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon


data class CustomSportFileDTO(

    val title: String,
    val description: String,
    val icon: SportIcon,
    val intervalLabel: String,

    ) : SportFileDTO()