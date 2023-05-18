package com.khalidtouch.classifiadmin.data.mapper

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ModelEntityModule {
    @Binds
    @Provides
    abstract fun provideModelMapper(impl: ModelEntityMapperImpl): ModelEntityMapper
}