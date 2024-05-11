package com.dgnt.quickScoreboardCreator.data.scoreboard.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScoreboardEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val scoreCarriesOver: Boolean,
//    val intervalListL: List<Pair<ScoreInfo, IntervalData>>
)