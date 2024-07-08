package com.dgnt.quickScoreboardCreator.ui.common

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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


inline fun <reified T : Parcelable?> parcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}