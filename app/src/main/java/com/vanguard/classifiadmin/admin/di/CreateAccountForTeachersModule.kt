package com.vanguard.classifiadmin.admin.di

import com.khalidtouch.chatme.network.CreateAccountForTeachers
import com.vanguard.classifiadmin.admin.teachers.CreateAccountForTeachersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CreateAccountForTeachersModule {
    @Binds
    @Singleton
    abstract fun bindCreateAccountForTeachers(impl: CreateAccountForTeachersImpl): CreateAccountForTeachers
}