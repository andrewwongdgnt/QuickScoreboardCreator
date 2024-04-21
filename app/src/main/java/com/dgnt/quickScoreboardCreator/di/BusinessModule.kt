package com.dgnt.quickScoreboardCreator.di

import android.app.Application
import androidx.room.Room
import com.dgnt.quickScoreboardCreator.common.util.GsonProvider
import com.dgnt.quickScoreboardCreator.data.db.QSCDatabase
import com.dgnt.quickScoreboardCreator.data.repository.QSCScoreboardRepository
import com.dgnt.quickScoreboardCreator.domain.repository.ScoreboardRepository
import com.dgnt.quickScoreboardCreator.domain.scoreboard.loader.QSCScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.loader.ScoreboardLoader
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
object BusinessModule {

    @Provides
    @Singleton
    fun provideScoreboardLoader(): ScoreboardLoader {
        return QSCScoreboardLoader(GsonProvider.gson)
    }

}