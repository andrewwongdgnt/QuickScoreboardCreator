package com.dgnt.quickScoreboardCreator.core.mapper

fun interface Mapper<F, T> {
    fun map(from: F): T
}