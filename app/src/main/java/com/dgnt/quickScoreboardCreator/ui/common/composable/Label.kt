package com.dgnt.quickScoreboardCreator.ui.common.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dgnt.quickScoreboardCreator.domain.Label

@Composable
fun Label.value() = when(this) {
    is Label.CustomLabel -> value
    is Label.ResourceLabel -> stringResource(id = res)
}