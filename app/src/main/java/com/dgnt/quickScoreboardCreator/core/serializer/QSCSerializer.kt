package com.dgnt.quickScoreboardCreator.core.serializer


import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.IntervalLabelData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.TeamLabelData
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.CustomSportFileDTO
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.DefaultSportFileDTO
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.SportFileDTOType
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory

class QSCSerializer: Serializer {

    private val gson: Gson = GsonBuilder().apply {
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(SportFileDTO::class.java, "type", true)
                .registerSubtype(DefaultSportFileDTO::class.java, SportFileDTOType.DEFAULT.name)
                .registerSubtype(CustomSportFileDTO::class.java, SportFileDTOType.CUSTOM.name)
        )
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(HistoricalIntervalRangeData::class.java, "type", true)
                .registerSubtype(HistoricalIntervalRangeData.CountDown::class.java, HistoricalIntervalRangeData.Type.COUNTDOWN.name)
                .registerSubtype(HistoricalIntervalRangeData.Infinite::class.java, HistoricalIntervalRangeData.Type.INFINITE.name)
        )
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(IntervalLabelData::class.java, "type", true)
                .registerSubtype(IntervalLabelData.Custom::class.java, IntervalLabelData.Type.CUSTOM.name)
                .registerSubtype(IntervalLabelData.DefaultSport::class.java, IntervalLabelData.Type.SPORT_TYPE.name)
        )
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(TeamLabelData::class.java, "type", true)
                .registerSubtype(TeamLabelData.None::class.java, TeamLabelData.Type.NONE.name)
                .registerSubtype(TeamLabelData.Custom::class.java, TeamLabelData.Type.CUSTOM.name)
        )

    }.create()

    override fun <T> serialize(value: T): String = gson.toJson(value)

    override fun <T> deserialize(value: String): T = gson.fromJson(value, object : TypeToken<T>() {}.type)

}
