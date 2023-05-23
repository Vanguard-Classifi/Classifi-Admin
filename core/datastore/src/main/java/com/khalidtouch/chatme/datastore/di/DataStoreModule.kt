package com.khalidtouch.chatme.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.khalidtouch.chatme.datastore.ComposeFeedDataProto
import com.khalidtouch.chatme.datastore.ComposeFeedMessageProto
import com.khalidtouch.chatme.datastore.FeedDataSerializer
import com.khalidtouch.chatme.datastore.FeedMessageSerializer
import com.khalidtouch.chatme.datastore.UserPreferences
import com.khalidtouch.chatme.datastore.UserPreferencesSerializer
import com.khalidtouch.core.common.ClassifiDispatcher
import com.khalidtouch.core.common.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(ClassifiDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    @Provides
    @Singleton
    fun provideFeedDataDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(ClassifiDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        feedDataSerializer: FeedDataSerializer,
    ): DataStore<ComposeFeedDataProto> =
        DataStoreFactory.create(
            serializer = feedDataSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob())
        ) { context.dataStoreFile("feed_data_preferences.pb") }

    @Provides
    @Singleton
    fun provideFeedMessageDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(ClassifiDispatcher.IO) ioDispatcher: CoroutineDispatcher,
        feedMessageSerializer: FeedMessageSerializer,
    ): DataStore<ComposeFeedMessageProto> =
        DataStoreFactory.create(
            serializer = feedMessageSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob())
        ) {
            context.dataStoreFile("feed_message_preferences.pb")
        }
}