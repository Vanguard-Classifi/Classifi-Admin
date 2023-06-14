package com.vanguard.classifiadmin.onboarding.di

import com.khalidtouch.chatme.network.CreateAccountForUsers
import com.vanguard.classifiadmin.onboarding.CreateAccountForUsersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CreateAccountForUsersModule {
    @Binds
    @Singleton
    abstract fun bindCreateAccountForTeachers(impl: CreateAccountForUsersImpl): CreateAccountForUsers
}