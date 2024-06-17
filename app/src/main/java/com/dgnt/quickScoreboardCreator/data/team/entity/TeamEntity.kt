package com.dgnt.quickScoreboardCreator.data.team.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon

@Entity
data class TeamEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val icon: TeamIcon
)