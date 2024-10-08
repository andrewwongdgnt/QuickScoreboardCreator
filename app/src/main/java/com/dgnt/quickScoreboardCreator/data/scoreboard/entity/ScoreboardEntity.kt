package com.dgnt.quickScoreboardCreator.data.scoreboard.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule

@Entity
data class ScoreboardEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val winRule: WinRule,
    val icon: ScoreboardIcon
//    val intervalListL: List<Pair<ScoreInfo, IntervalData>>
)