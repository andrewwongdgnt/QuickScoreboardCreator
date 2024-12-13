package com.dgnt.quickScoreboardCreator.core.data.serializer

import java.lang.reflect.Type

interface Serializer {

    fun <T> serialize(value: T): String

    fun <T> deserialize(value: String, type: Type): T
}