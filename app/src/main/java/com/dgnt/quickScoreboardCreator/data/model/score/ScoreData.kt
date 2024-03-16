package com.dgnt.quickScoreboardCreator.data.model.score

import com.dgnt.quickScoreboardCreator.data.model.BaseData

interface ScoreData<T> : BaseData<T> {
    val increments: List<T>

    fun update(incrementIndex: Int)
}