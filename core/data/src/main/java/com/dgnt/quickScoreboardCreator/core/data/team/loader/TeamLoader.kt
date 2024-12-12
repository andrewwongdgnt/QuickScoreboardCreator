package com.dgnt.quickScoreboardCreator.core.data.team.loader

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseLoader
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.core.data.team.config.TeamConfig
import java.io.InputStream

class TeamLoader(private val serializer: Serializer): BaseLoader<TeamConfig> {
    override fun import(inputStream: InputStream): TeamConfig? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            serializer.deserialize<TeamConfig>(data)


        } catch (e: Exception) {
            null
        } finally {
            inputStream.close()
        }
    }
}