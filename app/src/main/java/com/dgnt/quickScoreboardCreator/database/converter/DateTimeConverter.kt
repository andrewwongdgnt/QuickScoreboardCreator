package com.dgnt.quickScoreboardCreator.database.converter

import androidx.room.TypeConverter
import org.joda.time.DateTime

class DateTimeConverter {
    @TypeConverter
    fun toDate(value: Long?) = value?.let { DateTime(it) }

    @TypeConverter
    fun toEpoch(date: DateTime?) = date?.millis

}