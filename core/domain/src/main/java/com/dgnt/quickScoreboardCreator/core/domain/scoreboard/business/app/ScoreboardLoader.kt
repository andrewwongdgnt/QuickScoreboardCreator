package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.app

import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.config.ScoreboardConfig
import java.io.InputStream

fun interface ScoreboardLoader {
    operator fun invoke(inputStream: InputStream): ScoreboardConfig?
}