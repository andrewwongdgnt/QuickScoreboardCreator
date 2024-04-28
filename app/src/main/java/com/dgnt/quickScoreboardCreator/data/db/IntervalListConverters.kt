package com.dgnt.quickScoreboardCreator.data.db

import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.common.util.GsonProvider
import com.dgnt.quickScoreboardCreator.common.util.fromJson
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo

class IntervalListConverters {
    @TypeConverter
    fun fromJson(json: String): List<Pair<ScoreInfo<ScoreData>, IntervalData>> {
        return try {
            GsonProvider.gson.fromJson<ArrayList<Pair<ScoreInfo<ScoreData>, IntervalData>>>(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun toJson(value: List<Pair<ScoreInfo<ScoreData>, IntervalData>>): String {
        return GsonProvider.gson.toJson(value)
    }
}