package com.dgnt.quickScoreboardCreator.common.util


import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.DefaultScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardConfigType
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory

object GsonProvider {

    val gson:Gson = GsonBuilder().apply {
            registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory
                .of(ScoreboardConfig::class.java, "type", true)
                .registerSubtype(DefaultScoreboardConfig::class.java, ScoreboardConfigType.DEFAULT.name)
                .registerSubtype(CustomScoreboardConfig::class.java, ScoreboardConfigType.CUSTOM.name))

     }.create()

}

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)