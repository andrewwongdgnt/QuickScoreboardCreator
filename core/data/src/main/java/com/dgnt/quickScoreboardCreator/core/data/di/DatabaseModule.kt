package com.dgnt.quickScoreboardCreator.core.data.di

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.db.HistoricalScoreboardDataConverter
import com.dgnt.quickScoreboardCreator.core.data.db.IntervalListConverter
import com.dgnt.quickScoreboardCreator.core.data.db.QSCDatabase
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.data.history.filedao.HistoryFileDao
import com.dgnt.quickScoreboardCreator.core.data.history.filedto.HistoryFileDTO
import com.dgnt.quickScoreboardCreator.core.data.history.repository.QSCHistoryRepository
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.core.data.sport.entity.SportEntity
import com.dgnt.quickScoreboardCreator.core.data.sport.filedao.SportFileDao
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.core.data.sport.repository.QSCSportRepository
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.data.team.filedao.TeamFileDao
import com.dgnt.quickScoreboardCreator.core.data.team.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.core.data.team.repository.QSCTeamRepository
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.repository.SportRepository
import com.dgnt.quickScoreboardCreator.core.domain.team.model.TeamModel
import com.dgnt.quickScoreboardCreator.core.domain.team.repository.TeamRepository
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
        IntervalListConverter(serializer)

    @Provides
    @Singleton
    fun provideHistoricalScoreboardDataConverter(serializer: Serializer) =
        HistoricalScoreboardDataConverter(serializer)

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        intervalListConverter: IntervalListConverter,
        historicalScoreboardDataConverter: HistoricalScoreboardDataConverter
    ) =
        Room.databaseBuilder(
            app,
            QSCDatabase::class.java,
            "qsc_db"
        )
//            .addTypeConverter(intervalListConverter)
            .addTypeConverter(historicalScoreboardDataConverter)
            .build()

    @Provides
    @Singleton
    fun provideSportFileDao(serializer: Serializer): BaseFileDao<SportFileDTO> =
        SportFileDao(serializer)

    @Provides
    @Singleton
    fun provideSportRepository(
        db: QSCDatabase,
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
    fun provideTeamFileDao(serializer: Serializer): BaseFileDao<TeamFileDTO> =
        TeamFileDao(serializer)

    @Provides
    @Singleton
    fun provideTeamRepository(
        db: QSCDatabase,
        loader: BaseFileDao<TeamFileDTO>,
        mapTeamDataToDomain: Mapper<TeamEntity, TeamModel>,
        mapTeamDomainToData: Mapper<TeamModel, TeamEntity>,
        mapTeamFileDTOToDomain: Mapper<TeamFileDTO, TeamModel>,
    ): TeamRepository =
        QSCTeamRepository(
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
        db: QSCDatabase,
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