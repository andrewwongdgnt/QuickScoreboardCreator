package com.dgnt.quickScoreboardCreator.domain.common.mapper

fun interface Mapper<F, T> {
    fun map(from: F): T
}