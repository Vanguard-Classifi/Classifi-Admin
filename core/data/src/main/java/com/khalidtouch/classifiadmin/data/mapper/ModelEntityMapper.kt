package com.khalidtouch.classifiadmin.data.mapper

import com.khalidtouch.chatme.database.models.ClassifiAcademicSessionEntity
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.models.ClassifiCommentEntity
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity
import com.khalidtouch.chatme.database.models.ClassifiLikeEntity
import com.khalidtouch.chatme.database.models.ClassifiMessageEntity
import com.khalidtouch.chatme.database.models.ClassifiSchoolEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity
import com.khalidtouch.chatme.database.relations.UserWithSchools
import com.khalidtouch.classifiadmin.model.classifi.ClassifiAcademicSession
import com.khalidtouch.classifiadmin.model.classifi.ClassifiClass
import com.khalidtouch.classifiadmin.model.classifi.ClassifiComment
import com.khalidtouch.classifiadmin.model.classifi.ClassifiFeed
import com.khalidtouch.classifiadmin.model.classifi.ClassifiLike
import com.khalidtouch.classifiadmin.model.classifi.ClassifiMessage
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser


interface ModelEntityMapper {
    fun classEntityToModel(classifiClass: ClassifiClassEntity?): ClassifiClass?
    fun classModelToEntity(classifiClass: ClassifiClass?): ClassifiClassEntity?
    fun sessionEntityToModel(session: ClassifiAcademicSessionEntity?): ClassifiAcademicSession?
    fun sessionModelToEntity(session: ClassifiAcademicSession?): ClassifiAcademicSessionEntity?
    fun commentEntityToModel(comment: ClassifiCommentEntity?): ClassifiComment?
    fun commentModelToEntity(comment: ClassifiComment?): ClassifiCommentEntity?
    fun feedEntityToModel(feed: ClassifiFeedEntity?): ClassifiFeed?
    fun feedModelToEntity(feed: ClassifiFeed?): ClassifiFeedEntity?
    fun likeEntityToModel(like: ClassifiLikeEntity?): ClassifiLike?
    fun likeModelToEntity(like: ClassifiLike?): ClassifiLikeEntity?
    fun messageEntityToModel(message: ClassifiMessageEntity?): ClassifiMessage?
    fun messageModelToEntity(message: ClassifiMessage?): ClassifiMessageEntity?
    fun schoolEntityToModel(school: ClassifiSchoolEntity?): ClassifiSchool?
    fun schoolModelToEntity(school: ClassifiSchool?): ClassifiSchoolEntity?
    fun userEntityToModel(user: ClassifiUserEntity?): ClassifiUser?
    fun userEntityToModel2(user: UserWithSchools?): ClassifiUser?
    fun userModelToEntity(user: ClassifiUser?): ClassifiUserEntity?
    fun userModelToEntity2(user: ClassifiUser?): UserWithSchools?
}