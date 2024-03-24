package com.dgnt.quickScoreboardCreator.business.scoreboard.loader

import android.content.Context
import android.content.res.Resources
import com.dgnt.quickScoreboardCreator.business.scoreboard.model.config.ScoreboardConfig
import java.io.InputStream

interface ScoreboardLoader {
    fun load(resources: Resources, id: Int): ScoreboardConfig?
    fun load(context: Context, id: Int): ScoreboardConfig?
    fun load(inputStream: InputStream): ScoreboardConfig?
}