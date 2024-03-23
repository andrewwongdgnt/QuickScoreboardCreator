package com.dgnt.quickScoreboardCreator.business.score.loader

import android.content.Context

interface ScoreboardLoader {


    fun loadFromResource(context: Context, id: Int)
}