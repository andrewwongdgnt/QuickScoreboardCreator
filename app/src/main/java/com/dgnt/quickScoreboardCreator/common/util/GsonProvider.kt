package com.dgnt.quickScoreboardCreator.common.util

import com.dgnt.quickScoreboardCreator.domain.model.config.CustomScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardTemplate
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