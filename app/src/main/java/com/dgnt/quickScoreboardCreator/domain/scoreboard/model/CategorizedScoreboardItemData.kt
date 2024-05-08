package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import androidx.annotation.StringRes

data class CategorizedScoreboardItemData(
    @StringRes val titleRes: Int,
    val scoreboardItemDataList: List<ScoreboardItemData>
)