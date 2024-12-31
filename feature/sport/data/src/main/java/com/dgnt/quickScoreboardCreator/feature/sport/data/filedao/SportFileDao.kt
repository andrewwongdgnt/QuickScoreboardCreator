package com.dgnt.quickScoreboardCreator.feature.sport.data.filedao

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.IntervalFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.SportFileDTO
import com.google.gson.reflect.TypeToken
import java.io.InputStream

class SportFileDao(private val serializer: Serializer): BaseFileDao<SportFileDTO> {
    override fun import(inputStream: InputStream): SportFileDTO? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            serializer.deserialize<SportFileDTO>(data, object : TypeToken<SportFileDTO>() {}.type).apply {
                val size = repeatRule.map { it.to.size }.reduce { sum, element -> sum + element }
                val newIntervalList = MutableList<IntervalFileDTO?>(size) { null }
                repeatRule.forEach { repeatRuleFileDTO ->
                    repeatRuleFileDTO.to.forEach { to ->
                        newIntervalList[to] = intervalList[repeatRuleFileDTO.from]
                    }
                }
                intervalList = newIntervalList.filterNotNull()
            }


        } catch (e: Exception) {
            null
        } finally {
            inputStream.close()
        }
    }
}