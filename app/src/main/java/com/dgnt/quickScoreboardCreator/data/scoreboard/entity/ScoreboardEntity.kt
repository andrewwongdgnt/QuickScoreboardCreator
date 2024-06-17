package com.dgnt.quickScoreboardCreator.data.scoreboard.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon

@Entity
data class ScoreboardEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val icon: ScoreboardIcon
//    val intervalListL: List<Pair<ScoreInfo, IntervalData>>
)