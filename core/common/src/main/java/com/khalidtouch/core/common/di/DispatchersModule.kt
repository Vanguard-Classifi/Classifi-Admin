package com.khalidtouch.core.common.di

import com.khalidtouch.core.common.ClassifiDispatcher
import com.khalidtouch.core.common.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(ClassifiDispatcher.IO)
    fun providesIODispatchers() : CoroutineDispatcher = Dispatchers.IO
}