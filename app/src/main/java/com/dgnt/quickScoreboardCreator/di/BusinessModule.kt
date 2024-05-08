package com.dgnt.quickScoreboardCreator.di

import com.dgnt.quickScoreboardCreator.common.util.GsonProvider
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.QSCScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.QSCScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.QSCTimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.team.business.logic.QSCTeamCategorizer
import com.dgnt.quickScoreboardCreator.domain.team.business.logic.TeamCategorizer
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

    @Provides
    @Singleton
    fun provideTeamCategorizer(): TeamCategorizer =
        QSCTeamCategorizer()
}