package com.dgnt.quickScoreboardCreator.feature.team.data.filedao

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO
import com.google.gson.reflect.TypeToken
import java.io.InputStream

class TeamFileDao(private val serializer: Serializer): BaseFileDao<TeamFileDTO> {
    override fun import(inputStream: InputStream): TeamFileDTO? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            serializer.deserialize<TeamFileDTO>(data, object : TypeToken<TeamFileDTO>() {}.type)


        } catch (e: Exception) {
            null
        } finally {
            inputStream.close()
        }
    }
}