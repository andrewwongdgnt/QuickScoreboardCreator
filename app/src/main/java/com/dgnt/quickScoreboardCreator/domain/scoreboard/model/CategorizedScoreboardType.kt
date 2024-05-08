package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardType

data class CategorizedScoreboardType(
    @StringRes val titleRes: Int,
    val scoreboardTypeList: List<ScoreboardType>
)