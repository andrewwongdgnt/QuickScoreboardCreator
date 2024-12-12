package com.dgnt.quickScoreboardCreator.core.data.history.loader

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseLoader
import com.dgnt.quickScoreboardCreator.core.data.history.config.HistoryConfig
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import java.io.InputStream

class HistoryLoader(private val serializer: Serializer): BaseLoader<HistoryConfig> {
    override fun import(inputStream: InputStream): HistoryConfig? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            serializer.deserialize<HistoryConfig>(data)


        } catch (e: Exception) {
            null
        } finally {
            inputStream.close()
        }
    }
}