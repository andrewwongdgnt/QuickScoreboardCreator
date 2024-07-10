package com.dgnt.quickScoreboardCreator.data.db

import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.domain.common.GsonProvider
import com.dgnt.quickScoreboardCreator.domain.common.fromJson
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo

class IntervalListConverters {
    @TypeConverter
    fun fromJson(json: String): List<Pair<ScoreInfo, IntervalData>> {
        return try {
            GsonProvider.gson.fromJson<ArrayList<Pair<ScoreInfo, IntervalData>>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun toJson(value: List<Pair<ScoreInfo, IntervalData>>): String {
        return GsonProvider.gson.toJson(value)
    }
}