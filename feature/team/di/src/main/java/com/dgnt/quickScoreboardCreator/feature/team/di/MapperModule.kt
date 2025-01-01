package com.dgnt.quickScoreboardCreator.feature.team.di

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperDataToDomain
import com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperDomainToData
import com.dgnt.quickScoreboardCreator.feature.team.data.mapper.TeamMapperFileDTOToDomain
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



}