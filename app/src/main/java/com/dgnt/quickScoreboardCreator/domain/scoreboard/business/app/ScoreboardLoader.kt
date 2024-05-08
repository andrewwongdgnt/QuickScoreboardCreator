package com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app

import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.config.ScoreboardConfig
import java.io.InputStream

fun interface ScoreboardLoader {
    operator fun invoke(inputStream: InputStream): ScoreboardConfig?
}