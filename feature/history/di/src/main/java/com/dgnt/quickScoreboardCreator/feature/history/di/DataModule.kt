package com.dgnt.quickScoreboardCreator.feature.history.di

import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.feature.history.data.converter.HistoricalScoreboardDataConverter
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.data.filedao.HistoryFileDao
import com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.feature.history.data.repository.QSCHistoryRepository
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.repository.HistoryRepository
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
    fun provideHistoricalScoreboardDataConverter(serializer: Serializer) =
        HistoricalScoreboardDataConverter(serializer)

    @Provides
    @Singleton
    fun provideHistoryFileDao(serializer: Serializer): BaseFileDao<HistoryFileDTO> =
        HistoryFileDao(serializer)

    @Provides
    @Singleton
    fun provideHistoryRepository(
        historyDao: BaseDao<HistoryEntity>,
        historyFileDao: BaseFileDao<HistoryFileDTO>,
        mapHistoryDataToDomain: Mapper<HistoryEntity, HistoryModel>,
        mapHistoryDomainToData: Mapper<HistoryModel, HistoryEntity>,
        mapHistoryFileDTOToDomain: Mapper<HistoryFileDTO, HistoryModel>,
    ): HistoryRepository =
        QSCHistoryRepository(
            historyDao,
            historyFileDao,
            mapHistoryDataToDomain,
            mapHistoryDomainToData,
            mapHistoryFileDTOToDomain
        )
}