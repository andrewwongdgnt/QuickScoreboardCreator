package com.dgnt.quickScoreboardCreator.feature.team.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamIcon

@Entity
data class TeamEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val icon: TeamIcon
)