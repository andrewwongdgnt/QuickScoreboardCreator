package com.dgnt.quickScoreboardCreator.core.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer

@ProvidedTypeConverter
class HistoricalScoreboardDataConverter (private val serializer: Serializer) {
    @TypeConverter
    fun fromJson(json: String): HistoricalScoreboardData {
        return serializer.deserialize(json)
    }

    @TypeConverter
    fun toJson(value: HistoricalScoreboardData): String {
        return serializer.serialize(value)
    }
}