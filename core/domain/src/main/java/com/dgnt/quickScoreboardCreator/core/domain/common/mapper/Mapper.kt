package com.dgnt.quickScoreboardCreator.core.domain.common.mapper

fun interface Mapper<F, T> {
    fun map(from: F): T
}