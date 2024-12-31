package com.dgnt.quickScoreboardCreator.di

import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.HistoryCategorizer
import com.dgnt.quickScoreboardCreator.core.domain.history.business.logic.HistoryCreator
import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.QSCHistoryCategorizer
import com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.QSCHistoryCreator
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.QSCCategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.QSCScoreboardManager
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.QSCTimeTransformer
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.QSCWinCalculator
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.CategorizeSportUseCase
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.ScoreboardManager
import com.dgnt.quickScoreboardCreator.core.domain.sport.usecase.TimeTransformer
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.WinCalculator
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
    fun provideWinCalculator(): com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.WinCalculator =
        com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.QSCWinCalculator()

    @Provides
    fun provideScoreboardManager(winCalculator: com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.WinCalculator, historyCreator: HistoryCreator): com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.ScoreboardManager =
        com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.business.QSCScoreboardManager(winCalculator, historyCreator)

    @Provides
    @Singleton
    fun provideTimeTransformer(): TimeTransformer =
        QSCTimeTransformer()

    @Provides
    @Singleton
    fun provideTeamCategorizer(): com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.TeamCategorizer =
        com.dgnt.quickScoreboardCreator.feature.team.domain.business.logic.QSCTeamCategorizer()

    @Provides
    @Singleton
    fun provideScoreboardCategorizer(): CategorizeSportUseCase =
        QSCCategorizeSportUseCase()

    @Provides
    fun provideHistoryCreator(): HistoryCreator =
        com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.QSCHistoryCreator()

    @Provides
    @Singleton
    fun provideHistoryCategorizer(): com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.HistoryCategorizer =
        com.dgnt.quickScoreboardCreator.feature.history.domain.business.logic.QSCHistoryCategorizer()
}