package com.dgnt.quickScoreboardCreator.ui.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class TimelineViewerIdentifier(
    @Serializable
    val id: Int,
    @Serializable
    val index: Int
) : Parcelable