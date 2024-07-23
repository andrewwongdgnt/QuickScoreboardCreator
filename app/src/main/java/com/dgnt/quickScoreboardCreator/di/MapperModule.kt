package com.dgnt.quickScoreboardCreator.di

import com.dgnt.quickScoreboardCreator.data.history.data.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.domain.common.mapper.Mapper
import com.dgnt.quickScoreboardCreator.domain.history.mapper.HistoricalScoreboardMapperDataToDomain
import com.dgnt.quickScoreboardCreator.domain.history.mapper.HistoricalScoreboardMapperDomainToData
import com.dgnt.quickScoreboardCreator.domain.history.model.HistoricalScoreboard
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
    fun provideHistoricalScoreboardMapperDomainToData(): Mapper<HistoricalScoreboard, HistoricalScoreboardData> =
        HistoricalScoreboardMapperDomainToData()

    @Singleton
    @Provides
    fun provideHistoricalScoreboardMapperDataToDomain(): Mapper<HistoricalScoreboardData, HistoricalScoreboard> =
        HistoricalScoreboardMapperDataToDomain()

}