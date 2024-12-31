package com.dgnt.quickScoreboardCreator.di

import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.HistoryCategorizer
import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.HistoryCreator
import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.QSCHistoryCategorizer
import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.QSCHistoryCreator
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.QSCScoreboardManager
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.QSCWinCalculator
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.ScoreboardManager
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.WinCalculator
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.CategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.QSCCategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.QSCTimeTransformer
import com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase.TimeTransformer
import com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.QSCTeamCategorizer
import com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.TeamCategorizer
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
    fun provideScoreboardCategorizer(): CategorizeSportUseCase =
        QSCCategorizeSportUseCase()

    @Provides
    fun provideHistoryCreator(): HistoryCreator =
        QSCHistoryCreator()

    @Provides
    @Singleton
    fun provideHistoryCategorizer(): HistoryCategorizer =
        QSCHistoryCategorizer()
}