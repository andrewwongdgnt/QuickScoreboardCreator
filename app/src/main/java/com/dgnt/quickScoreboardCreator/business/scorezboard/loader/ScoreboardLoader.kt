package com.dgnt.quickScoreboardCreator.business.scorezboard.loader

import android.content.Context

interface ScoreboardLoader {


    fun loadFromResource(context: Context, id: Int)
}