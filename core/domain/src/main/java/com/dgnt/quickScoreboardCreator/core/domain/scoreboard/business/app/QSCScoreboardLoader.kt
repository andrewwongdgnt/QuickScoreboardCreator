package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.app

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.IntervalConfig
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.ScoreboardConfig
import com.google.gson.Gson
import java.io.InputStream

class QSCScoreboardLoader(private val gson: Gson) : ScoreboardLoader {

    override fun invoke(inputStream: InputStream): ScoreboardConfig? {
        return try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }
            gson.fromJson(data, ScoreboardConfig::class.java).apply {
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