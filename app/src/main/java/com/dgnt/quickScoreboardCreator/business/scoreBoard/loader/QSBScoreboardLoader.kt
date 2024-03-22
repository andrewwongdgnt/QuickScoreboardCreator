package com.dgnt.quickScoreboardCreator.business.scoreBoard.loader

import android.content.Context
import android.util.Log
import com.dgnt.quickScoreboardCreator.R
import com.google.gson.Gson

class QSBScoreboardLoader(gson: Gson) {

    companion object {
        private const val TAG = "QSBScoreboardLoader"
    }

    fun loadFromResource(context: Context, id: Int) {
        val inputStream = context.resources.openRawResource(id)
        try {
            val data = inputStream.bufferedReader().use {
                it.readText()
            }

        } catch (e: Exception) {
            Log.e(TAG, "error parsing when loading from resource",e)
        } finally {
            inputStream.close()
        }
    }
}