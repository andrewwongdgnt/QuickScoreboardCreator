package com.dgnt.quickScoreboardCreator.core.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.ScoreInfo
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class IntervalListConverter (private val serializer: Serializer) {
    @TypeConverter
    fun fromJson(json: String): List<Pair<ScoreInfo, IntervalData>> {
        return try {
            serializer.deserialize(json, object : TypeToken<List<Pair<ScoreInfo, IntervalData>>>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun toJson(value: List<Pair<ScoreInfo, IntervalData>>): String {
        return serializer.serialize(value)
    }
}