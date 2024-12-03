package com.dgnt.quickScoreboardCreator.di

import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoricalScoreboardMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoricalScoreboardMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoryMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoryMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.mapper.ScoreboardMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.mapper.ScoreboardMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.data.team.mapper.TeamMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.team.mapper.TeamMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper

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

    @Singleton
    @Provides
    fun provideScoreboardMapperDomainToData(): Mapper<ScoreboardModel, ScoreboardEntity> =
        ScoreboardMapperDomainToData()

    @Singleton
    @Provides
    fun provideScoreboardMapperDataToDomain(): Mapper<ScoreboardEntity, ScoreboardModel> =
        ScoreboardMapperDataToDomain()

    @Singleton
    @Provides
    fun provideTeamMapperDomainToData(): Mapper<TeamModel, TeamEntity> =
        TeamMapperDomainToData()

    @Singleton
    @Provides
    fun provideTeamMapperDataToDomain(): Mapper<TeamEntity, TeamModel> =
        TeamMapperDataToDomain()

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

}