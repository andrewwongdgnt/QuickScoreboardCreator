package com.dgnt.quickScoreboardCreator.core.data.sport.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportIcon
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule

@Entity
data class SportEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val winRule: WinRule,
    val icon: SportIcon,
    val intervalLabel: String
//    val intervalList: List<Pair<ScoreInfo, IntervalData>>
)