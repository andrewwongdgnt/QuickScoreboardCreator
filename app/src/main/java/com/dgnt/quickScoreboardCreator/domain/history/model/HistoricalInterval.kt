package com.dgnt.quickScoreboardCreator.domain.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class HistoricalInterval(
    val range: HistoricalIntervalRange,
    val intervalLabel: IntervalLabel,
    val historicalScoreGroupList: Map<Int, HistoricalScoreGroup>
) : Parcelable

@Serializable
@Parcelize
sealed interface HistoricalIntervalRange : Parcelable {
    @Serializable
    @Parcelize
    data class CountDown(val start: Long) : HistoricalIntervalRange
    @Serializable
    @Parcelize
    data object Infinite : HistoricalIntervalRange
}