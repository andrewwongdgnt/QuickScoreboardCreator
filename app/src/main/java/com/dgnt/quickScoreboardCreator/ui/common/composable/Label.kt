package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


sealed interface Label {
    data class CustomLabel(val value: String) : Label
    data class ResourceLabel(@StringRes val res: Int) : Label
}

@Composable
fun Label.value() = when (this) {
    is Label.CustomLabel -> value
    is Label.ResourceLabel -> stringResource(id = res)
}