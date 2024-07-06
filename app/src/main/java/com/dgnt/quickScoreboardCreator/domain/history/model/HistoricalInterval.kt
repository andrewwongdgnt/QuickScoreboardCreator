package com.dgnt.quickScoreboardCreator.domain.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class HistoricalInterval(
    val historicalScoreGroupList: Map<Int, HistoricalScoreGroup>
) : Parcelable
