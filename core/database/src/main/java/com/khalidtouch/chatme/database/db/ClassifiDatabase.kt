package com.khalidtouch.chatme.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.khalidtouch.chatme.database.converter.DateConverter
import com.khalidtouch.chatme.database.converter.MessageTypeConverter
import com.khalidtouch.chatme.database.converter.UserRoleConverter
import com.khalidtouch.chatme.database.dao.ClassDao
import com.khalidtouch.chatme.database.dao.CommentDao
import com.khalidtouch.chatme.database.dao.FeedDao
import com.khalidtouch.chatme.database.dao.LikeDao
import com.khalidtouch.chatme.database.dao.MessageDao
import com.khalidtouch.chatme.database.dao.SchoolDao
import com.khalidtouch.chatme.database.dao.SessionDao
import com.khalidtouch.chatme.database.dao.UserDao
import com.khalidtouch.chatme.database.models.ClassifiAcademicSessionEntity
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.models.ClassifiCommentEntity
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity
import com.khalidtouch.chatme.database.models.ClassifiLikeEntity
import com.khalidtouch.chatme.database.models.ClassifiMessageEntity
import com.khalidtouch.chatme.database.models.ClassifiSchoolEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity
import com.khalidtouch.chatme.database.relations.FeedsWithClassesCrossRef
import com.khalidtouch.chatme.database.relations.UsersWithClasses
import com.khalidtouch.chatme.database.relations.UsersWithClassesCrossRef
import com.khalidtouch.chatme.database.relations.UsersWithSchoolsCrossRef


@Database(
    entities = [
        ClassifiUserEntity::class,
        ClassifiSchoolEntity::class,
        ClassifiMessageEntity::class,
        ClassifiLikeEntity::class,
        ClassifiFeedEntity::class,
        ClassifiCommentEntity::class,
        ClassifiClassEntity::class,
        ClassifiAcademicSessionEntity::class,
        UsersWithSchoolsCrossRef::class,
        UsersWithClassesCrossRef::class,
        FeedsWithClassesCrossRef::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    DateConverter::class,
    MessageTypeConverter::class,
    UserRoleConverter::class,
)
abstract class ClassifiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun classDao(): ClassDao
    abstract fun commentDao(): CommentDao
    abstract fun feedDao(): FeedDao
    abstract fun likeDao(): LikeDao
    abstract fun messageDao(): MessageDao
    abstract fun schoolDao(): SchoolDao
}