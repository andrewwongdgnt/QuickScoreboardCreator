package com.dgnt.quickScoreboardCreator.feature.sport.di

import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.sport.data.entity.SportEntity
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.mapper.SportMapperDataToDomain
import com.dgnt.quickScoreboardCreator.feature.sport.data.mapper.SportMapperDomainToData
import com.dgnt.quickScoreboardCreator.feature.sport.data.mapper.SportMapperFileDTOToDomain
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel


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



}