package com.dgnt.quickScoreboardCreator.domain.history.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
sealed class IntervalLabel(val index: Int) : Parcelable {
    @Serializable
    @Parcelize
    data class CustomIntervalLabel(val value: String, val i: Int = -1) : IntervalLabel(i)

    @Serializable
    @Parcelize
    data class ResourceIntervalLabel(@StringRes val res: Int, val i: Int = -1) : IntervalLabel(i)

    fun duplicateWithIndex(index: Int) =
        when (this) {
            is CustomIntervalLabel -> CustomIntervalLabel(this.value, index)
            is ResourceIntervalLabel -> ResourceIntervalLabel(this.res, index)
        }

}