package com.dgnt.quickScoreboardCreator.business.scoreboard.loader

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.config.ScoreboardConfig
import com.google.gson.Gson
import java.io.InputStream

class QSCScoreboardLoader(val gson: Gson) : ScoreboardLoader {

    override fun load(resources: Resources, id: Int) =
        load(resources.openRawResource(id))


    override fun load(context: Context, id: Int) =
        load(context.resources, id)


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