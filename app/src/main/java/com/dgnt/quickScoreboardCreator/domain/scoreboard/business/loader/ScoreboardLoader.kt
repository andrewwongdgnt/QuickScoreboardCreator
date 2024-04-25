package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.loader

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardConfig
import java.io.InputStream

fun interface ScoreboardLoader {
    fun load(inputStream: InputStream): ScoreboardConfig?
}