package com.dgnt.quickScoreboardCreator.data.db

import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.core.gson.GsonProvider
import com.dgnt.quickScoreboardCreator.core.gson.fromJson
import com.dgnt.quickScoreboardCreator.data.history.entity.HistoricalScoreboardData

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