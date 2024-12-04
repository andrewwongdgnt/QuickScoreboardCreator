package com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


sealed interface Label {
    data class Custom(val value: String) : Label
    data class Resource(@StringRes val res: Int) : Label
}

@Composable
fun Label.value() = when (this) {
    is Label.Custom -> value
    is Label.Resource -> stringResource(id = res)
}