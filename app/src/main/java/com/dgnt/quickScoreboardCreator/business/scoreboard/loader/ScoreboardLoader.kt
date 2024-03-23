package com.dgnt.quickScoreboardCreator.business.scoreboard.loader

import android.content.Context

interface ScoreboardLoader {


    fun loadFromResource(context: Context, id: Int)
}