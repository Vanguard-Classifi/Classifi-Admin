package com.vanguard.classifiadmin.di

import com.vanguard.classifiadmin.domain.downloader.AndroidDownloader
import com.vanguard.classifiadmin.domain.downloader.Downloader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadModule {
    @Binds
    @Singleton
    abstract fun provideDownloader(impl: AndroidDownloader): Downloader
}