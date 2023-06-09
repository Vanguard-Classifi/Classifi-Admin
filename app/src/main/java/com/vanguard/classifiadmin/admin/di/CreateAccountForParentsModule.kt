package com.vanguard.classifiadmin.admin.di

import com.khalidtouch.chatme.network.CreateAccountForParents
import com.vanguard.classifiadmin.admin.parents.CreateAccountForParentsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CreateAccountForParentsModule {
    @Binds
    @Singleton
    abstract fun bindCreateAccountForParents(impl: CreateAccountForParentsImpl): CreateAccountForParents
}