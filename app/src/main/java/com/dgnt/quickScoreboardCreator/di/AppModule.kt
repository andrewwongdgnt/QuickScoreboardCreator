package com.dgnt.quickScoreboardCreator.di

import android.app.Application
import android.content.res.Resources
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.data.db.QSCDatabase
import com.dgnt.quickScoreboardCreator.data.history.repository.QSCHistoryRepository
import com.dgnt.quickScoreboardCreator.data.scoreboard.repository.QSCScoreboardRepository
import com.dgnt.quickScoreboardCreator.data.team.repository.QSCTeamRepository
import com.dgnt.quickScoreboardCreator.domain.history.repository.HistoryRepository
import com.dgnt.quickScoreboardCreator.domain.scoreboard.repository.ScoreboardRepository
import com.dgnt.quickScoreboardCreator.domain.team.repository.TeamRepository
import com.dgnt.quickScoreboardCreator.ui.common.uievent.QSCUiEventHandler
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
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
    fun provideTeamRepository(db: QSCDatabase): TeamRepository =
        QSCTeamRepository(db.teamDao)

    @Provides
    @Singleton
    fun provideHistoryRepository(db: QSCDatabase): HistoryRepository =
        QSCHistoryRepository(db.historyDao)

    @Provides
    fun provideUiEventHandler(): UiEventHandler =
        QSCUiEventHandler()

}