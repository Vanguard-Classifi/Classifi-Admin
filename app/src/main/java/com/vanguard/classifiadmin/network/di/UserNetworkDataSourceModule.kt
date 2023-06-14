package com.vanguard.classifiadmin.network.di

import com.khalidtouch.chatme.network.UserNetworkDataSource
import com.vanguard.classifiadmin.network.UserNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class UserNetworkDataSourceModule {
    @Binds
    @Singleton
    abstract fun bind(impl: UserNetworkDataSourceImpl): UserNetworkDataSource
}