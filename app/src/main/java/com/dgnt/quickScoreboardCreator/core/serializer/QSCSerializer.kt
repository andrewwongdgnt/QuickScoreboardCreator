package com.dgnt.quickScoreboardCreator.core.serializer


import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.IntervalLabelData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.TeamLabelData
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.ConfigType
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory

class QSCSerializer: Serializer {

    private val gson: Gson = GsonBuilder().apply {
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

    override fun <T> serialize(value: T): String = gson.toJson(value)

    override fun <T> deserialize(value: String): T = gson.fromJson(value, object : TypeToken<T>() {}.type)

}
