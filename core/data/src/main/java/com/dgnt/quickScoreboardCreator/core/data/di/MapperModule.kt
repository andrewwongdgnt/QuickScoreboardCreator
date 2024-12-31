package com.dgnt.quickScoreboardCreator.core.data.di

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoricalScoreboardMapperDataToDomain
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoricalScoreboardMapperDomainToData
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperDataToDomain
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperDomainToData
import com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperFileDTOToDomain
import com.dgnt.quickScoreboardCreator.core.data.sport.entity.SportEntity
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.core.data.sport.mapper.SportMapperDataToDomain
import com.dgnt.quickScoreboardCreator.core.data.sport.mapper.SportMapperDomainToData
import com.dgnt.quickScoreboardCreator.core.data.sport.mapper.SportMapperFileDTOToDomain
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperDataToDomain
import com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperDomainToData
import com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperFileDTOToDomain
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoricalScoreboard
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
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
    fun provideTeamMapperDomainToData(): Mapper<com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel, com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity> =
        com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperDomainToData()

    @Singleton
    @Provides
    fun provideTeamMapperDataToDomain(): Mapper<com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity, com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel> =
        com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperDataToDomain()

    @Singleton
    @Provides
    fun provideTeamMapperConfigToDomain(): Mapper<com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO, com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel> =
        com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperFileDTOToDomain()

    @Singleton
    @Provides
    fun provideHistoryMapperDomainToData(
        historicalScoreboardMapperDomainToData: Mapper<HistoricalScoreboard, com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData>
    ): Mapper<com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel, com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity> =
        com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperDomainToData(historicalScoreboardMapperDomainToData)

    @Singleton
    @Provides
    fun provideHistoryMapperDataToDomain(
        historicalScoreboardMapperDataToDomain: Mapper<com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData, HistoricalScoreboard>
    ): Mapper<com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity, com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel> =
        com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperDataToDomain(historicalScoreboardMapperDataToDomain)

    @Singleton
    @Provides
    fun provideHistoryMapperConfigToDomain(): Mapper<com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO, com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel> =
        com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoryMapperFileDTOToDomain()

    @Singleton
    @Provides
    fun provideHistoricalScoreboardMapperDomainToData(): Mapper<HistoricalScoreboard, com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData> =
        com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoricalScoreboardMapperDomainToData()

    @Singleton
    @Provides
    fun provideHistoricalScoreboardMapperDataToDomain(): Mapper<com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoricalScoreboardData, HistoricalScoreboard> =
        com.dgnt.quickScoreboardCreator.feature.history.data.mapper.HistoricalScoreboardMapperDataToDomain()

}