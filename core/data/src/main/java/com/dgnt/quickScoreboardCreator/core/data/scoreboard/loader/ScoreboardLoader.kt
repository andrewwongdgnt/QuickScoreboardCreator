package com.dgnt.quickScoreboardCreator.core.data.scoreboard.loader

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseLoader
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.IntervalConfig
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import java.io.InputStream

class ScoreboardLoader(private val serializer: Serializer): BaseLoader<ScoreboardConfig> {
    override fun import(inputStream: InputStream): ScoreboardConfig? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            serializer.deserialize<ScoreboardConfig>(data).apply {
                val size = repeatRule.map { it.to.size }.reduce { sum, element -> sum + element }
                val newIntervalList = MutableList<IntervalConfig?>(size) { null }
                repeatRule.forEach { repeatRuleConfig ->
                    repeatRuleConfig.to.forEach { to ->
                        newIntervalList[to] = intervalList[repeatRuleConfig.from]
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