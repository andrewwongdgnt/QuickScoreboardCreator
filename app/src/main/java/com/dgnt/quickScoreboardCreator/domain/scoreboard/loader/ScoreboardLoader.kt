package com.dgnt.quickScoreboardCreator.domain.scoreboard.loader

import android.content.Context
import android.content.res.Resources
import com.dgnt.quickScoreboardCreator.domain.model.config.ScoreboardConfig
import java.io.InputStream

interface ScoreboardLoader {
    fun load(resources: Resources, id: Int): ScoreboardConfig?
    fun load(context: Context, id: Int): ScoreboardConfig?
    fun load(inputStream: InputStream): ScoreboardConfig?
}