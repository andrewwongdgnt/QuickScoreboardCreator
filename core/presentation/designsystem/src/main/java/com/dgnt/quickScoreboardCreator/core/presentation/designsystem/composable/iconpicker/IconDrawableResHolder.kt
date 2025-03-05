package com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker

import androidx.annotation.DrawableRes

data class IconDrawableResHolder<T>(
    val originalIcon: T,
    @DrawableRes val res: Int
)