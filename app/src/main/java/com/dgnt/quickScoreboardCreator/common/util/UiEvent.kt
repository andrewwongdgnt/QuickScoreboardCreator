package com.dgnt.quickScoreboardCreator.common.util

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class UiEvent {
    data object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    sealed class ShowSnackbar(
        open val message: Int,
        open val action: Int? = null
    ) : UiEvent() {
        class ShowGenericSnackbar(
            @StringRes override val message: Int,
            @StringRes override val action: Int? = null
        ) : ShowSnackbar(message, action)

        data class ShowQuantitySnackbar(
            @PluralsRes override val message: Int,
            val quantity: Int,
            @StringRes override val action: Int
        ) : ShowSnackbar(message, action)
    }
}
