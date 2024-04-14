package com.dgnt.quickScoreboardCreator.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScoreboardEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String
)