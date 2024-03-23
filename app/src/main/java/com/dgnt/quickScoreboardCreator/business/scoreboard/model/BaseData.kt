package com.dgnt.quickScoreboardCreator.business.scoreboard.model

interface BaseData<T> {
    val initial: T
    var current: T

    fun reset(){
        current = initial
    }
}