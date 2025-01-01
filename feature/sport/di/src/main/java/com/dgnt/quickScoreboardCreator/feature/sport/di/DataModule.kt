package com.dgnt.quickScoreboardCreator.feature.sport.di

import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.feature.sport.data.converter.IntervalListConverter
import com.dgnt.quickScoreboardCreator.feature.sport.data.entity.SportEntity
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedao.SportFileDao
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.data.repository.QSCSportRepository
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.repository.SportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideIntervalListConverter(serializer: Serializer) =
        IntervalListConverter(serializer)

    @Provides
    @Singleton
    fun provideSportFileDao(serializer: Serializer): BaseFileDao<SportFileDTO> =
        SportFileDao(serializer)

    @Provides
    @Singleton
    fun provideSportRepository(
        sportDao: BaseDao<SportEntity>,
        sportFileDao: BaseFileDao<SportFileDTO>,
        mapScoreboardDataToDomain: Mapper<SportEntity, SportModel>,
        mapScoreboardDomainToData: Mapper<SportModel, SportEntity>,
        mapScoreboardConfigToDomain: Mapper<SportFileDTO, SportModel>,
    ): SportRepository =
        QSCSportRepository(
            sportDao,
            sportFileDao,
            mapScoreboardDataToDomain,
            mapScoreboardDomainToData,
            mapScoreboardConfigToDomain
        )
}