package com.dgnt.quickScoreboardCreator.di

import com.dgnt.quickScoreboardCreator.core.domain.history.business.logic.HistoryCategorizer
import com.dgnt.quickScoreboardCreator.core.domain.history.business.logic.HistoryCreator
import com.dgnt.quickScoreboardCreator.core.domain.history.business.logic.QSCHistoryCategorizer
import com.dgnt.quickScoreboardCreator.core.domain.history.business.logic.QSCHistoryCreator
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.QSCScoreboardCategorizer
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.QSCScoreboardManager
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.QSCTimeTransformer
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.QSCWinCalculator
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.ScoreboardCategorizer
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.ScoreboardManager
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.TimeTransformer
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.business.WinCalculator
import com.dgnt.quickScoreboardCreator.core.domain.team.business.logic.QSCTeamCategorizer
import com.dgnt.quickScoreboardCreator.core.domain.team.business.logic.TeamCategorizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BusinessModule {

    @Provides
    fun provideWinCalculator(): WinCalculator =
        QSCWinCalculator()

    @Provides
    fun provideScoreboardManager(winCalculator: WinCalculator, historyCreator: HistoryCreator): ScoreboardManager =
        QSCScoreboardManager(winCalculator, historyCreator)

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

    @Provides
    @Singleton
    fun provideHistoryCategorizer(): HistoryCategorizer =
        QSCHistoryCategorizer()
}