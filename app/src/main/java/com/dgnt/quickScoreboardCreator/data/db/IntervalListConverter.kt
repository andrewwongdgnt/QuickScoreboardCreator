package com.dgnt.quickScoreboardCreator.data.db

import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.core.gson.GsonProvider
import com.dgnt.quickScoreboardCreator.core.gson.fromJson
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo

class IntervalListConverter {
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