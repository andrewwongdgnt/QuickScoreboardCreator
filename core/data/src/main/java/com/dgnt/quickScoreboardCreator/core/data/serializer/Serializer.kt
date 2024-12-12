package com.dgnt.quickScoreboardCreator.core.data.serializer

interface Serializer {

    fun <T> serialize(value: T): String

    fun <T> deserialize(value: String): T
}