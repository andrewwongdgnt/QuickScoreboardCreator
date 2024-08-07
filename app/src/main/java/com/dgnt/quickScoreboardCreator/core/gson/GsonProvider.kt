package com.dgnt.quickScoreboardCreator.core.gson


import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.data.history.data.IntervalLabelData
import com.dgnt.quickScoreboardCreator.data.history.data.TeamLabelData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ConfigType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory

object GsonProvider {

    val gson: Gson = GsonBuilder().apply {
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(ScoreboardConfig::class.java, "type", true)
                .registerSubtype(DefaultScoreboardConfig::class.java, ConfigType.DEFAULT.name)
                .registerSubtype(CustomScoreboardConfig::class.java, ConfigType.CUSTOM.name)
        )
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(HistoricalIntervalRangeData::class.java, "type", true)
                .registerSubtype(HistoricalIntervalRangeData.CountDown::class.java, HistoricalIntervalRangeData.Type.COUNTDOWN.name)
                .registerSubtype(HistoricalIntervalRangeData.Infinite::class.java, HistoricalIntervalRangeData.Type.INFINITE.name)
        )
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(IntervalLabelData::class.java, "type", true)
                .registerSubtype(IntervalLabelData.Custom::class.java, IntervalLabelData.Type.CUSTOM.name)
                .registerSubtype(IntervalLabelData.ScoreboardType::class.java, IntervalLabelData.Type.SCOREBOARD_TYPE.name)
        )
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(TeamLabelData::class.java, "type", true)
                .registerSubtype(TeamLabelData.None::class.java, TeamLabelData.Type.NONE.name)
                .registerSubtype(TeamLabelData.Custom::class.java, TeamLabelData.Type.CUSTOM.name)
        )

    }.create()

}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)