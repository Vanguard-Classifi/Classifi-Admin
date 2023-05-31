package com.vanguard.classifiadmin.network.di

import com.khalidtouch.chatme.network.SchoolNetworkDataSource
import com.vanguard.classifiadmin.network.SchoolNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SchoolNetworkDataSourceModule {

    @Binds
    @Singleton
    abstract fun bind(impl: SchoolNetworkDataSourceImpl): SchoolNetworkDataSource
}