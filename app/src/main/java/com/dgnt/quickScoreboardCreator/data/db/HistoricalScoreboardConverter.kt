package com.dgnt.quickScoreboardCreator.data.db

import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.domain.common.GsonProvider
import com.dgnt.quickScoreboardCreator.domain.common.fromJson
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard

class HistoricalScoreboardConverter {
    @TypeConverter
    fun fromJson(json: String): HistoricalScoreboard {
        return GsonProvider.gson.fromJson<HistoricalScoreboard>(json)
    }

    @TypeConverter
    fun toJson(value: HistoricalScoreboard): String {
        return GsonProvider.gson.toJson(value)
    }
}