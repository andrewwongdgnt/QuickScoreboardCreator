package com.dgnt.quickScoreboardCreator.domain.team.model

import androidx.annotation.StringRes
import com.dgnt.quickScoreboardCreator.R

enum class TeamIconGroup(@StringRes val res: Int) {
    ANIMALS(R.string.ic_group_animals),
    ARTIFACTS(R.string.ic_group_artifacts),
    NATURE(R.string.ic_group_nature),
    PEOPLE(R.string.ic_group_people),
    SCIFI(R.string.ic_group_scifi),
    VEHICLES(R.string.ic_group_vehicles),
    WEAPONS(R.string.ic_group_weapons),
}