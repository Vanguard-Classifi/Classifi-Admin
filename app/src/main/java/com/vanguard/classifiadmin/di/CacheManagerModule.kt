package com.vanguard.classifiadmin.di

import com.vanguard.classifiadmin.data.file.CacheManagerImpl
import com.vanguard.classifiadmin.domain.helpers.CacheManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheManagerModule {
    @Binds
    @Singleton
    abstract fun provideCacheManager(impl: CacheManagerImpl): CacheManager
}