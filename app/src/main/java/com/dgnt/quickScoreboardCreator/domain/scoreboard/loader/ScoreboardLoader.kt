package com.dgnt.quickScoreboardCreator.domain.scoreboard.loader

import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardConfig
import java.io.InputStream

fun interface ScoreboardLoader {
    fun load(inputStream: InputStream): ScoreboardConfig?
}