package com.dgnt.quickScoreboardCreator.di

import android.app.Application
import android.content.res.Resources
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.QSCUiEventHandler
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEventHandler
import com.dgnt.quickScoreboardCreator.core.serializer.QSCSerializer
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
    @Singleton
    fun provideSerializer(): Serializer =
        QSCSerializer()

    @Provides
    fun provideUiEventHandler(): UiEventHandler =
        QSCUiEventHandler()

}