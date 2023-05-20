package com.khalidtouch.classifiadmin.data.di

import com.khalidtouch.chatme.domain.repository.ClassRepository
import com.khalidtouch.chatme.domain.repository.CommentRepository
import com.khalidtouch.chatme.domain.repository.FeedRepository
import com.khalidtouch.chatme.domain.repository.LikeRepository
import com.khalidtouch.chatme.domain.repository.MessageRepository
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.chatme.domain.repository.SessionRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstClassRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstCommentRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstFeedRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstLikeRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstMessageRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstSchoolRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstSchoolRepository_Factory
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstSessionRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstUserDataRepository
import com.khalidtouch.classifiadmin.data.repository.OfflineFirstUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideClassRepository(impl: OfflineFirstClassRepository): ClassRepository

    @Binds
    @Singleton
    abstract fun provideCommentRepository(impl: OfflineFirstCommentRepository): CommentRepository


    @Binds
    @Singleton
    abstract fun provideNewsFeedRepository(impl: OfflineFirstFeedRepository): FeedRepository

    @Binds
    @Singleton
    abstract fun provideLikeRepository(impl: OfflineFirstLikeRepository): LikeRepository

    @Binds
    @Singleton
    abstract fun provideMessageRepository(impl: OfflineFirstMessageRepository): MessageRepository

    @Binds
    @Singleton
    abstract fun provideSchoolRepository(impl: OfflineFirstSchoolRepository): SchoolRepository

    @Binds
    @Singleton
    abstract fun provideSessionRepository(impl: OfflineFirstSessionRepository): SessionRepository

    @Binds
    @Singleton
    abstract fun provideUserDataRepository(impl: OfflineFirstUserDataRepository): UserDataRepository

    @Binds
    @Singleton
    abstract fun provideUserRepository(impl: OfflineFirstUserRepository): UserRepository
}