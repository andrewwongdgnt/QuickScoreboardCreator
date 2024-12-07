package com.dgnt.quickScoreboardCreator.core.data.di

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.db.QSCDatabase
import com.dgnt.quickScoreboardCreator.core.data.history.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.core.data.history.repository.QSCHistoryRepository
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.entity.ScoreboardEntity
import com.dgnt.quickScoreboardCreator.core.data.scoreboard.repository.QSCScoreboardRepository
import com.dgnt.quickScoreboardCreator.core.data.team.entity.TeamEntity
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
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(
            app,
            QSCDatabase::class.java,
            "qsc_db"
        ).build()

    @Provides
    @Singleton
    fun provideScoreboardRepository(
        db: QSCDatabase,
        mapScoreboardDataToDomain: Mapper<ScoreboardEntity, ScoreboardModel>,
        mapScoreboardDomainToData: Mapper<ScoreboardModel, ScoreboardEntity>,
    ): ScoreboardRepository =
        QSCScoreboardRepository(
            db.scoreboardDao,
            mapScoreboardDataToDomain,
            mapScoreboardDomainToData
        )

    @Provides
    @Singleton
    fun provideTeamRepository(
        db: QSCDatabase,
        mapTeamDataToDomain: Mapper<TeamEntity, TeamModel>,
        mapTeamDomainToData: Mapper<TeamModel, TeamEntity>,
    ): TeamRepository =
        QSCTeamRepository(
            db.teamDao,
            mapTeamDataToDomain,
            mapTeamDomainToData
        )

    @Provides
    @Singleton
    fun provideHistoryRepository(
        db: QSCDatabase,
        mapHistoryDataToDomain: Mapper<HistoryEntity, HistoryModel>,
        mapHistoryDomainToData: Mapper<HistoryModel, HistoryEntity>,
    ): HistoryRepository =
        QSCHistoryRepository(
            db.historyDao,
            mapHistoryDataToDomain,
            mapHistoryDomainToData
        )
}