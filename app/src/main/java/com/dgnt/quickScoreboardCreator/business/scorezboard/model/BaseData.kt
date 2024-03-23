package com.dgnt.quickScoreboardCreator.data.model

interface BaseData<T> {
    val initial: T
    var current: T

    fun reset(){
        current = initial
    }
}