package com.dgnt.quickScoreboardCreator.feature.scoreboard.di

import com.dgnt.quickScoreboardCreator.feature.history.domain.usecase.CreateHistoryUseCase
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.manager.QSCScoreboardManager
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.manager.ScoreboardManager
import com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.usecase.CalculateWinUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BusinessModule {

    @Provides
    fun provideScoreboardManager(
        calculateWinUseCase: CalculateWinUseCase,
        createHistoryUseCase: CreateHistoryUseCase
    ): ScoreboardManager =
        QSCScoreboardManager(calculateWinUseCase, createHistoryUseCase)
}