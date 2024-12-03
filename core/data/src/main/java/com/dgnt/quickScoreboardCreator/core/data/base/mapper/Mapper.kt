package com.dgnt.quickScoreboardCreator.core.data.base.mapper

fun interface Mapper<F, T> {
    fun map(from: F): T
}