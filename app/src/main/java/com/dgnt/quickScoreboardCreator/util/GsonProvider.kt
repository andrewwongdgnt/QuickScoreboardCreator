package com.dgnt.quickScoreboardCreator.util

import com.dgnt.quickScoreboardCreator.business.scoreboard.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.config.ScoreboardTemplate
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory

object GsonProvider {

    val gson:Gson = GsonBuilder().apply {
            registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory
                .of(ScoreboardConfig::class.java, "type", true)
                .registerSubtype(ScoreboardTemplate::class.java, "TEMPLATE")
                .registerSubtype(CustomScoreboardConfig::class.java, "CUSTOM"))

     }.create()

}