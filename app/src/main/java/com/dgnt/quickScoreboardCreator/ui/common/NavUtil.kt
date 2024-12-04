package com.dgnt.quickScoreboardCreator.ui.common

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable

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


inline fun <reified T : Serializable?> customNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getSerializable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getSerializable(key) as? T
        }

    override fun parseValue(value: String): T = json.decodeFromString(Uri.decode(value))

    override fun serializeAsValue(value: T) = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) =
        bundle.putSerializable(key, value)

}