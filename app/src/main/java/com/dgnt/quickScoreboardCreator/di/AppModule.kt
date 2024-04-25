package com.dgnt.quickScoreboardCreator.di

import android.app.Application
import android.content.res.Resources
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.data.base.db.QSCDatabase
import com.dgnt.quickScoreboardCreator.data.scoreboard.repository.QSCScoreboardRepository
import com.dgnt.quickScoreboardCreator.data.team.repository.QSCTeamRepository
import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.DeleteScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.scoreboard.usecase.InsertScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.team.repository.TeamRepository
import com.dgnt.quickScoreboardCreator.domain.team.usecase.DeleteTeamListUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamListUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.GetTeamUseCase
import com.dgnt.quickScoreboardCreator.domain.team.usecase.InsertTeamListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideResources(app: Application): Resources =
        app.resources

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
    fun provideScoreboardRepository(db: QSCDatabase): ScoreboardRepository =
        QSCScoreboardRepository(db.scoreboardDao)

    @Provides
    @Singleton
    fun provideGetScoreboardListUseCase(repository: ScoreboardRepository) =
        GetScoreboardListUseCase(repository)

    @Provides
    @Singleton
    fun provideGetScoreboardUseCase(repository: ScoreboardRepository) =
        GetScoreboardUseCase(repository)

    @Provides
    @Singleton
    fun provideInsertScoreboardListUseCase(repository: ScoreboardRepository) =
        InsertScoreboardListUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteScoreboardListUseCase(repository: ScoreboardRepository) =
        DeleteScoreboardListUseCase(repository)

    @Provides
    @Singleton
    fun provideTeamRepository(db: QSCDatabase): TeamRepository =
        QSCTeamRepository(db.teamDao)

    @Provides
    @Singleton
    fun provideGetTeamListUseCase(repository: TeamRepository) =
        GetTeamListUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTeamUseCase(repository: TeamRepository) =
        GetTeamUseCase(repository)

    @Provides
    @Singleton
    fun provideInsertTeamListUseCase(repository: TeamRepository) =
        InsertTeamListUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteTeamListUseCase(repository: TeamRepository) =
        DeleteTeamListUseCase(repository)

}