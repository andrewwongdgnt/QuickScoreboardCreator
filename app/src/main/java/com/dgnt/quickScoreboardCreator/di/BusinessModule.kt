package com.dgnt.quickScoreboardCreator.di

import com.dgnt.quickScoreboardCreator.common.util.GsonProvider
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.loader.QSCScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.loader.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager.QSCScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager.QSCTimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager.ScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.manager.TimeTransformer
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
    fun provideScoreboardLoader(): ScoreboardLoader =
        QSCScoreboardLoader(GsonProvider.gson)

    @Provides
    @Singleton
    fun provideScoreboardManager(): ScoreboardManager =
        QSCScoreboardManager()

    @Provides
    @Singleton
    fun provideTimeTransformer(): TimeTransformer =
        QSCTimeTransformer()
}