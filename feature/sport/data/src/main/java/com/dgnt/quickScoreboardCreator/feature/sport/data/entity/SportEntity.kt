package com.dgnt.quickScoreboardCreator.feature.sport.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule


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