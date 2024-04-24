package com.dgnt.quickScoreboardCreator.ui.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.ui.common.Routes


sealed class Screen(
    @StringRes val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
) {
    data object ScoreboardList : Screen(R.string.scoreboardListNavTitle, Icons.Filled.Home, Icons.Outlined.Home, Routes.SCOREBOARD_LIST)
    data object TeamList : Screen(R.string.teamListNavTitle, Icons.Filled.Person, Icons.Outlined.Person, Routes.TEAM_LIST)
    data object Contact : Screen(R.string.contactNavTitle, Icons.Filled.Email, Icons.Outlined.Email, Routes.CONTACT)
}