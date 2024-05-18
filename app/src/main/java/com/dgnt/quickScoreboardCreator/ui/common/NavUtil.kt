package com.dgnt.quickScoreboardCreator.ui.common

import androidx.navigation.NavController

fun NavController.commonNavigate(currentDestination: androidx.navigation.NavDestination? = null, navDestination: NavDestination) = navigate(navDestination) {
    // Avoid building up a large stack of destinations
    // on the back stack as users select items
    currentDestination?.id?.let {
        popUpTo(it) {
            saveState = true
            inclusive = true
        }
    } ?: run {
        popUpTo(navDestination) {
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