package com.dgnt.quickScoreboardCreator.core.database

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.feature.history.data.converter.HistoricalScoreboardDataConverter
import com.dgnt.quickScoreboardCreator.feature.sport.data.converter.IntervalListConverter
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


}