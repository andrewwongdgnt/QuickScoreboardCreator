package com.dgnt.quickScoreboardCreator.feature.sport.domain.model

interface BaseData<T> {
    val initial: T
    var current: T

    fun reset(){
        current = initial
    }
}