package com.vanguard.classifiadmin.di

import com.vanguard.classifiadmin.data.preferences.PrefDatastore
import com.vanguard.classifiadmin.data.preferences.PrefDatastoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class PrefModule {
    @Binds
    @Singleton
    abstract fun providePrefDatastore(impl: PrefDatastoreImpl): PrefDatastore
}