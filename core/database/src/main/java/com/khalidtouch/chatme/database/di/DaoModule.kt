package com.khalidtouch.chatme.database.di

import com.khalidtouch.chatme.database.dao.ClassDao
import com.khalidtouch.chatme.database.dao.CommentDao
import com.khalidtouch.chatme.database.dao.FeedDao
import com.khalidtouch.chatme.database.dao.LikeDao
import com.khalidtouch.chatme.database.dao.MessageDao
import com.khalidtouch.chatme.database.dao.SchoolDao
import com.khalidtouch.chatme.database.dao.SessionDao
import com.khalidtouch.chatme.database.dao.UserDao
import com.khalidtouch.chatme.database.db.ClassifiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    @Singleton
    fun provideUserDao(db: ClassifiDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideSessionDao(db: ClassifiDatabase): SessionDao = db.sessionDao()


    @Provides
    @Singleton
    fun provideSchoolDao(db: ClassifiDatabase): SchoolDao = db.schoolDao()

    @Provides
    @Singleton
    fun provideMessageDao(db: ClassifiDatabase): MessageDao = db.messageDao()


    @Provides
    @Singleton
    fun provideLikeDao(db: ClassifiDatabase): LikeDao = db.likeDao()

    @Provides
    @Singleton
    fun provideFeedDao(db: ClassifiDatabase): FeedDao = db.feedDao()


    @Provides
    @Singleton
    fun provideCommentDao(db: ClassifiDatabase): CommentDao = db.commentDao()


    @Provides
    @Singleton
    fun provideClassDao(db: ClassifiDatabase): ClassDao = db.classDao()
}