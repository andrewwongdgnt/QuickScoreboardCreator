package com.dgnt.quickScoreboardCreator.feature.history.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class HistoricalScoreboardDataConverter(private val serializer: Serializer) {
    @TypeConverter
    fun fromJson(json: String): HistoricalScoreboardData {
       val s =  ScoreInfo(ScoreRule.None, mapOf(), "", listOf())
        val i = IntervalData(0,0)

        return serializer.deserialize(json, object : TypeToken<HistoricalScoreboardData>() {}.type)
    }

    @TypeConverter
    fun toJson(value: HistoricalScoreboardData): String {
        return serializer.serialize(value)
    }
}