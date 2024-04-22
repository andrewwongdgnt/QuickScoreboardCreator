package com.dgnt.quickScoreboardCreator.domain.scoreboard.loader

import android.content.Context
import android.content.res.Resources
import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardConfig
import com.google.gson.Gson
import java.io.InputStream

class QSCScoreboardLoader(private val gson: Gson) : ScoreboardLoader {

    override fun load(inputStream: InputStream): ScoreboardConfig? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            gson.fromJson(data, ScoreboardConfig::class.java)

        } catch (e: Exception) {
            null
        } finally {
            inputStream.close()
        }
    }
}