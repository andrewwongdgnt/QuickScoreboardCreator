package com.dgnt.quickScoreboardCreator.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonProvider {

    val gson:Gson

    init{
       gson = GsonBuilder().apply {
//            registerTypeAdapter()

        }.create()
    }
}