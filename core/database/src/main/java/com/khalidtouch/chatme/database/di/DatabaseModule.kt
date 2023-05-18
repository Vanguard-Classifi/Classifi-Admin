package com.khalidtouch.chatme.database.di

import android.content.Context
import androidx.room.Room
import com.khalidtouch.chatme.database.db.ClassifiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideClassifiDatabase(
        @ApplicationContext context: Context,
    ): ClassifiDatabase =
        Room.databaseBuilder(
            context,
            ClassifiDatabase::class.java,
            "Classifi-Database",
        ).build()
}