package com.dgnt.quickScoreboardCreator.feature.history.data.filedao

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.google.gson.reflect.TypeToken
import java.io.InputStream

class HistoryFileDao(private val serializer: Serializer): BaseFileDao<HistoryFileDTO> {
    override fun import(inputStream: InputStream): HistoryFileDTO? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            serializer.deserialize<HistoryFileDTO>(data, object : TypeToken<HistoryFileDTO>() {}.type)


        } catch (e: Exception) {
            null
        } finally {
            inputStream.close()
        }
    }
}