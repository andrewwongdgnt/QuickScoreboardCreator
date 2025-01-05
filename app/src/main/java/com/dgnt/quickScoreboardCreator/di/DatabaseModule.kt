package com.dgnt.quickScoreboardCreator.di

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.database.QSCDatabase
import com.dgnt.quickScoreboardCreator.feature.history.data.converter.HistoricalScoreboardDataConverter
import com.dgnt.quickScoreboardCreator.feature.history.data.entity.HistoryEntity
import com.dgnt.quickScoreboardCreator.feature.sport.data.converter.IntervalListConverter
import com.dgnt.quickScoreboardCreator.feature.sport.data.entity.SportEntity
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity
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
    fun provideSportDao(db: QSCDatabase): BaseDao<SportEntity> = db.sportDao

    @Provides
    @Singleton
    fun provideTeamDao(db: QSCDatabase): BaseDao<TeamEntity> = db.teamDao

    @Provides
    @Singleton
    fun provideHistoryDao(db: QSCDatabase): BaseDao<HistoryEntity> = db.historyDao
}