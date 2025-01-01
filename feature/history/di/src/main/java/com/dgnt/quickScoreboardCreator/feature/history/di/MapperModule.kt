package com.dgnt.quickScoreboardCreator.feature.history.di

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoricalScoreboardMapperDataToDomain
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoricalScoreboardMapperDomainToData
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperDataToDomain
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperDomainToData
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperFileDTOToDomain
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {


    @Singleton
    @Provides
    fun provideHistoryMapperDomainToData(
        historicalScoreboardMapperDomainToData: Mapper<HistoricalScoreboard, HistoricalScoreboardData>
    ): Mapper<HistoryModel, HistoryEntity> =
        HistoryMapperDomainToData(historicalScoreboardMapperDomainToData)

    @Singleton
    @Provides
    fun provideHistoryMapperDataToDomain(
        historicalScoreboardMapperDataToDomain: Mapper<HistoricalScoreboardData, HistoricalScoreboard>
    ): Mapper<HistoryEntity, HistoryModel> =
        HistoryMapperDataToDomain(historicalScoreboardMapperDataToDomain)

    @Singleton
    @Provides
    fun provideHistoryMapperConfigToDomain(): Mapper<HistoryFileDTO, HistoryModel> =
        HistoryMapperFileDTOToDomain()

    @Singleton
    @Provides
    fun provideHistoricalScoreboardMapperDomainToData(): Mapper<HistoricalScoreboard, HistoricalScoreboardData> =
        HistoricalScoreboardMapperDomainToData()

    @Singleton
    @Provides
    fun provideHistoricalScoreboardMapperDataToDomain(): Mapper<HistoricalScoreboardData, HistoricalScoreboard> =
        HistoricalScoreboardMapperDataToDomain()

}