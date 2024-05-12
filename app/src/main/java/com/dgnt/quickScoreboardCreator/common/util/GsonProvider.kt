package com.dgnt.quickScoreboardCreator.common.util


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ConfigType
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.CustomIntervalDataConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultIntervalDataConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.IntervalDataConfig
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
                .of(IntervalDataConfig::class.java, "type", true)
                .registerSubtype(DefaultIntervalDataConfig::class.java, ConfigType.DEFAULT.name)
                .registerSubtype(CustomIntervalDataConfig::class.java, ConfigType.CUSTOM.name)
        )

    }.create()

}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)