package com.dgnt.quickScoreboardCreator.ui.common

import kotlinx.serialization.Serializable


@Serializable
data class TimelineViewerIdentifier(
    @Serializable
    val id: Int,
    @Serializable
    val index: Int
): java.io.Serializable