package com.dgnt.quickScoreboardCreator.core.data.team.filedao

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.core.data.team.filedto.TeamFileDTO
import java.io.InputStream

class TeamFileDao(private val serializer: Serializer): BaseFileDao<TeamFileDTO> {
    override fun import(inputStream: InputStream): TeamFileDTO? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            serializer.deserialize<TeamFileDTO>(data)


        } catch (e: Exception) {
            null
        } finally {
            inputStream.close()
        }
    }
}