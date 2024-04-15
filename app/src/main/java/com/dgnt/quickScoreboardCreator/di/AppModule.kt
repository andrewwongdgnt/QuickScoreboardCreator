package com.dgnt.quickScoreboardCreator.di

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.data.db.QSCDatabase
import com.dgnt.quickScoreboardCreator.data.repository.QSCScoreboardRepository
import com.dgnt.quickScoreboardCreator.domain.repository.ScoreboardRepository
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
}