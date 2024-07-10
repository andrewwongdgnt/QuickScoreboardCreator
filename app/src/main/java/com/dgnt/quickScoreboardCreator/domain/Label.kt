package com.dgnt.quickScoreboardCreator.domain

import androidx.annotation.StringRes

sealed interface Label {
    data class CustomLabel(val value: String) : Label
    data class ResourceLabel(@StringRes val res: Int) : Label
}