package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model

interface BaseData<T> {
    val initial: T
    var current: T

    fun reset(){
        current = initial
    }
}