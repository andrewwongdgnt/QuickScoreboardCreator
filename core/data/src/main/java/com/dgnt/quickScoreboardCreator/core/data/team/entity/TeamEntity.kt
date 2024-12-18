package com.dgnt.quickScoreboardCreator.core.data.team.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamIcon

@Entity
data class TeamEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String,
    val icon: TeamIcon
)