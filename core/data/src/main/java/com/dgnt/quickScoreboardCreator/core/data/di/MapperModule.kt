package com.dgnt.quickScoreboardCreator.core.data.di

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.data.history.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoricalScoreboardMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoricalScoreboardMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoryMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoryMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.data.history.mapper.HistoryMapperFileDTOToDomain
import com.dgnt.quickScoreboardCreator.core.data.sport.entity.SportEntity
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.core.data.sport.mapper.SportMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.sport.mapper.SportMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.data.sport.mapper.SportMapperFileDTOToDomain
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.data.team.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.core.data.team.mapper.TeamMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.team.mapper.TeamMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.data.team.mapper.TeamMapperFileDTOToDomain
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
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
    fun provideSportMapperDomainToData(): Mapper<SportModel, SportEntity> =
        SportMapperDomainToData()

    @Singleton
    @Provides
    fun provideSportMapperDataToDomain(): Mapper<SportEntity, SportModel> =
        SportMapperDataToDomain()

    @Singleton
    @Provides
    fun provideSportMapperConfigToDomain(): Mapper<SportFileDTO, SportModel> =
        SportMapperFileDTOToDomain()

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
    fun provideTeamMapperConfigToDomain(): Mapper<TeamFileDTO, TeamModel> =
        TeamMapperFileDTOToDomain()

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