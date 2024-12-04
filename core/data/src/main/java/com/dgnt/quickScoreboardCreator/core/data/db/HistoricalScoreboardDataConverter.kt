package com.dgnt.quickScoreboardCreator.core.data.db

import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.core.data.util.GsonProvider
import com.dgnt.quickScoreboardCreator.core.data.util.fromJson


class HistoricalScoreboardDataConverter {
    @TypeConverter
    fun fromJson(json: String): HistoricalScoreboardData {
        return GsonProvider.gson.fromJson<HistoricalScoreboardData>(json)
    }

    @TypeConverter
    fun toJson(value: HistoricalScoreboardData): String {
        return GsonProvider.gson.toJson(value)
    }
}