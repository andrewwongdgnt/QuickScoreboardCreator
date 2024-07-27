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
import com.dgnt.quickScoreboardCreator.ui.common.NavDestination
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.Timeline


sealed class NavItem(
    @StringRes val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val navDestination: NavDestination
) {
    data object ScoreboardList : NavItem(R.string.scoreboardListNavTitle, Icons.Filled.Home, Icons.Outlined.Home, NavDestination.ScoreboardList)
    data object TeamList : NavItem(R.string.teamListNavTitle, Icons.Filled.Person, Icons.Outlined.Person, NavDestination.TeamList)
    data object HistoryList : NavItem(R.string.historyListNavTitle, Icons.Filled.Timeline, Icons.Filled.Timeline, NavDestination.HistoryList)
    data object Contact : NavItem(R.string.contactNavTitle, Icons.Filled.Email, Icons.Outlined.Email, NavDestination.Contact)
}

