package com.dgnt.quickScoreboardCreator.domain.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class HistoricalScoreGroup(
    val primaryScoreList: List<HistoricalScore>,
    val secondaryScoreList: List<HistoricalScore>,
) : Parcelable

