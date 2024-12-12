package com.dgnt.quickScoreboardCreator.core.data.di

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseLoader
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.db.HistoricalScoreboardDataConverter
import com.dgnt.quickScoreboardCreator.core.data.db.IntervalListConverter
import com.dgnt.quickScoreboardCreator.core.data.db.QSCDatabase
import com.dgnt.quickScoreboardCreator.core.data.history.config.HistoryConfig
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.data.history.loader.HistoryLoader
import com.dgnt.quickScoreboardCreator.core.data.history.repository.QSCHistoryRepository
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.config.ScoreboardConfig
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.loader.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.repository.QSCScoreboardRepository
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.core.data.team.config.TeamConfig
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.core.data.team.loader.TeamLoader
import com.dgnt.quickScoreboardCreator.core.data.team.repository.QSCTeamRepository
import com.dgnt.quickScoreboardCreator.core.domain.history.model.HistoryModel
import com.dgnt.quickScoreboardCreator.core.domain.history.repository.HistoryRepository
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardModel
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.repository.ScoreboardRepository
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
    fun provideScoreboardLoader(serializer: Serializer): BaseLoader<ScoreboardConfig> =
        ScoreboardLoader(serializer)

    @Provides
    @Singleton
    fun provideScoreboardRepository(
        db: QSCDatabase,
        loader: BaseLoader<ScoreboardConfig>,
        mapScoreboardDataToDomain: Mapper<ScoreboardEntity, ScoreboardModel>,
        mapScoreboardDomainToData: Mapper<ScoreboardModel, ScoreboardEntity>,
        mapScoreboardConfigToDomain: Mapper<ScoreboardConfig, ScoreboardModel>,
    ): ScoreboardRepository =
        QSCScoreboardRepository(
            db.scoreboardDao,
            loader,
            mapScoreboardDataToDomain,
            mapScoreboardDomainToData,
            mapScoreboardConfigToDomain
        )

    @Provides
    @Singleton
    fun provideTeamLoader(serializer: Serializer): BaseLoader<TeamConfig> =
        TeamLoader(serializer)

    @Provides
    @Singleton
    fun provideTeamRepository(
        db: QSCDatabase,
        loader: BaseLoader<TeamConfig>,
        mapTeamDataToDomain: Mapper<TeamEntity, TeamModel>,
        mapTeamDomainToData: Mapper<TeamModel, TeamEntity>,
        mapTeamConfigToDomain: Mapper<TeamConfig, TeamModel>,
    ): TeamRepository =
        QSCTeamRepository(
            db.teamDao,
            loader,
            mapTeamDataToDomain,
            mapTeamDomainToData,
            mapTeamConfigToDomain
        )

    @Provides
    @Singleton
    fun provideHistoryLoader(serializer: Serializer): BaseLoader<HistoryConfig> =
        HistoryLoader(serializer)

    @Provides
    @Singleton
    fun provideHistoryRepository(
        db: QSCDatabase,
        loader: BaseLoader<HistoryConfig>,
        mapHistoryDataToDomain: Mapper<HistoryEntity, HistoryModel>,
        mapHistoryDomainToData: Mapper<HistoryModel, HistoryEntity>,
        mapHistoryConfigToDomain: Mapper<HistoryConfig, HistoryModel>,
    ): HistoryRepository =
        QSCHistoryRepository(
            db.historyDao,
            loader,
            mapHistoryDataToDomain,
            mapHistoryDomainToData,
            mapHistoryConfigToDomain
        )
}