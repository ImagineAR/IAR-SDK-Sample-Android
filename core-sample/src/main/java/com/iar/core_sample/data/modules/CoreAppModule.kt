package com.iar.core_sample.data.modules

import com.iar.core_sample.data.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreAppModule {

    @Provides
    @Singleton
    fun provideAppConfig() = AppConfig()
}