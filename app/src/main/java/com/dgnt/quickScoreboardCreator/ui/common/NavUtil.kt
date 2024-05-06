package com.dgnt.quickScoreboardCreator.ui.common

import androidx.navigation.NavController
import androidx.navigation.NavDestination

fun NavController.commonNavigate(currentDestination: NavDestination? = null, route: String) = navigate(route) {
    // Avoid building up a large stack of destinations
    // on the back stack as users select items
    currentDestination?.id?.let {
        popUpTo(it) {
            saveState = true
            inclusive = true
        }
    } ?: run {
        popUpTo(route) {
            saveState = true
            inclusive = true
        }
    }
    // Avoid multiple copies of the same destination when
    // reselecting the same item
    launchSingleTop = true
    // Restore state when reselecting a previously selected item
    restoreState = true
}