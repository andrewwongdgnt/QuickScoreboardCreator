package com.dgnt.quickScoreboardCreator.core.data.di

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.feature.history.data.converter.HistoricalScoreboardDataConverter
import com.dgnt.quickScoreboardCreator.feature.sport.data.converter.IntervalListConverter
import com.dgnt.quickScoreboardCreator.core.database.QSCDatabase
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.history.data.filedao.HistoryFileDao
import com.dgnt.quickScoreboardCreator.feature.history.data.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.feature.history.data.repository.QSCHistoryRepository
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.core.data.sport.entity.SportEntity
import com.dgnt.quickScoreboardCreator.core.data.sport.filedao.SportFileDao
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.core.data.sport.repository.QSCSportRepository
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.feature.team.data.filedao.TeamFileDao
import com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.feature.team.data.repository.QSCTeamRepository
import com.dgnt.quickScoreboardCreator.feature.history.domain.model.HistoryModel
import com.dgnt.quickScoreboardCreator.feature.history.domain.repository.HistoryRepository
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.repository.SportRepository
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.repository.TeamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideIntervalListConverter(serializer: Serializer) =
        com.dgnt.quickScoreboardCreator.feature.sport.data.converter.IntervalListConverter(serializer)

    @Provides
    @Singleton
    fun provideHistoricalScoreboardDataConverter(serializer: Serializer) =
        com.dgnt.quickScoreboardCreator.feature.history.data.converter.HistoricalScoreboardDataConverter(serializer)


    @Provides
    @Singleton
    fun provideSportFileDao(serializer: Serializer): BaseFileDao<SportFileDTO> =
        SportFileDao(serializer)

    @Provides
    @Singleton
    fun provideSportRepository(
        db: com.dgnt.quickScoreboardCreator.core.database.QSCDatabase,
        loader: BaseFileDao<SportFileDTO>,
        mapScoreboardDataToDomain: Mapper<SportEntity, SportModel>,
        mapScoreboardDomainToData: Mapper<SportModel, SportEntity>,
        mapScoreboardConfigToDomain: Mapper<SportFileDTO, SportModel>,
    ): SportRepository =
        QSCSportRepository(
            db.sportDao,
            loader,
            mapScoreboardDataToDomain,
            mapScoreboardDomainToData,
            mapScoreboardConfigToDomain
        )

    @Provides
    @Singleton
    fun provideTeamFileDao(serializer: Serializer): BaseFileDao<com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO> =
        com.dgnt.quickScoreboardCreator.feature.team.data.filedao.TeamFileDao(serializer)

    @Provides
    @Singleton
    fun provideTeamRepository(
        db: com.dgnt.quickScoreboardCreator.core.database.QSCDatabase,
        loader: BaseFileDao<com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO>,
        mapTeamDataToDomain: Mapper<com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity, com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel>,
        mapTeamDomainToData: Mapper<com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel, com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity>,
        mapTeamFileDTOToDomain: Mapper<com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO, com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel>,
    ): com.dgnt.quickScoreboardCreator.feature.team.domain.repository.TeamRepository =
        com.dgnt.quickScoreboardCreator.feature.team.data.repository.QSCTeamRepository(
            db.teamDao,
            loader,
            mapTeamDataToDomain,
            mapTeamDomainToData,
            mapTeamFileDTOToDomain
        )

    @Provides
    @Singleton
    fun provideHistoryFileDao(serializer: Serializer): BaseFileDao<HistoryFileDTO> =
        HistoryFileDao(serializer)

    @Provides
    @Singleton
    fun provideHistoryRepository(
        db: com.dgnt.quickScoreboardCreator.core.database.QSCDatabase,
        loader: BaseFileDao<HistoryFileDTO>,
        mapHistoryDataToDomain: Mapper<HistoryEntity, HistoryModel>,
        mapHistoryDomainToData: Mapper<HistoryModel, HistoryEntity>,
        mapHistoryFileDTOToDomain: Mapper<HistoryFileDTO, HistoryModel>,
    ): HistoryRepository =
        QSCHistoryRepository(
            db.historyDao,
            loader,
            mapHistoryDataToDomain,
            mapHistoryDomainToData,
            mapHistoryFileDTOToDomain
        )
}