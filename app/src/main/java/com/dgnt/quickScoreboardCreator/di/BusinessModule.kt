package com.dgnt.quickScoreboardCreator.di

import com.dgnt.quickScoreboardCreator.common.util.GsonProvider
import com.dgnt.quickScoreboardCreator.domain.history.business.logic.HistoryCreator
import com.dgnt.quickScoreboardCreator.domain.history.business.logic.QSCHistoryCreator
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.QSCScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.app.ScoreboardLoader
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.QSCScoreboardCategorizer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.QSCScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.QSCTimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.QSCWinCalculator
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardCategorizer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.ScoreboardManager
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.TimeTransformer
import com.dgnt.quickScoreboardCreator.domain.scoreboard.business.logic.WinCalculator
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
    fun provideWinCalculator(): WinCalculator =
        QSCWinCalculator()

    @Provides
    fun provideScoreboardManager(winCalculator: WinCalculator): ScoreboardManager =
        QSCScoreboardManager(winCalculator)

    @Provides
    @Singleton
    fun provideTimeTransformer(): TimeTransformer =
        QSCTimeTransformer()

    @Provides
    @Singleton
    fun provideTeamCategorizer(): TeamCategorizer =
        QSCTeamCategorizer()

    @Provides
    @Singleton
    fun provideScoreboardCategorizer(): ScoreboardCategorizer =
        QSCScoreboardCategorizer()

    @Provides
    fun provideHistoryCreator(): HistoryCreator =
        QSCHistoryCreator()
}