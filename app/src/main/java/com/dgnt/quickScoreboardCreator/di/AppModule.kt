package com.dgnt.quickScoreboardCreator.di

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.data.db.QSCDatabase
import com.dgnt.quickScoreboardCreator.data.repository.QSCScoreboardRepository
import com.dgnt.quickScoreboardCreator.domain.repository.ScoreboardRepository
import com.dgnt.quickScoreboardCreator.domain.usecase.DeleteScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.usecase.GetScoreboardListUseCase
import com.dgnt.quickScoreboardCreator.domain.usecase.GetScoreboardUseCase
import com.dgnt.quickScoreboardCreator.domain.usecase.InsertScoreboardListUseCase
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
    fun provideDatabase(app: Application): QSCDatabase {
        return Room.databaseBuilder(
            app,
            QSCDatabase::class.java,
            "qsc_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideScoreboardRepository(db: QSCDatabase): ScoreboardRepository {
        return QSCScoreboardRepository(db.scoreboardDao)
    }

    @Provides
    @Singleton
    fun provideGetScoreboardListUseCase(repository: ScoreboardRepository): GetScoreboardListUseCase {
        return GetScoreboardListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetScoreboardUseCase(repository: ScoreboardRepository): GetScoreboardUseCase {
        return GetScoreboardUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideInsertScoreboardUseCase(repository: ScoreboardRepository): InsertScoreboardListUseCase {
        return InsertScoreboardListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteScoreboardListUseCase(repository: ScoreboardRepository): DeleteScoreboardListUseCase {
        return DeleteScoreboardListUseCase(repository)
    }
}