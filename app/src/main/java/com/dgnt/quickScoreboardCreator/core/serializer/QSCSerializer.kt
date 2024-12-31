package com.dgnt.quickScoreboardCreator.core.serializer


import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.CustomSportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.DefaultSportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.SportFileDTOType
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import java.lang.reflect.Type

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
                .of(com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData::class.java, "type", true)
                .registerSubtype(com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData.CountDown::class.java, com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData.Type.COUNTDOWN.name)
                .registerSubtype(com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData.Infinite::class.java, com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalIntervalRangeData.Type.INFINITE.name)
        )
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData::class.java, "type", true)
                .registerSubtype(com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData.Custom::class.java, com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData.Type.CUSTOM.name)
                .registerSubtype(com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData.DefaultSport::class.java, com.dgnt.quickScoreboardCreator.feature.history.data.entity.IntervalLabelData.Type.SPORT_TYPE.name)
        )
        registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                .of(com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData::class.java, "type", true)
                .registerSubtype(com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData.None::class.java, com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData.Type.NONE.name)
                .registerSubtype(com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData.Custom::class.java, com.dgnt.quickScoreboardCreator.feature.history.data.entity.TeamLabelData.Type.CUSTOM.name)
        )

    }.create()

    override fun <T> serialize(value: T): String = gson.toJson(value)

    override fun <T> deserialize(value: String, type: Type): T {
        return gson.fromJson(value, type)
    }

}
