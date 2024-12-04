package com.dgnt.quickScoreboardCreator.di

import android.app.Application
import android.content.res.Resources
import com.dgnt.quickScoreboardCreator.ui.common.uievent.QSCUiEventHandler
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEventHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideResources(app: Application): Resources =
        app.resources

    @Provides
    fun provideUiEventHandler(): UiEventHandler =
        QSCUiEventHandler()

}