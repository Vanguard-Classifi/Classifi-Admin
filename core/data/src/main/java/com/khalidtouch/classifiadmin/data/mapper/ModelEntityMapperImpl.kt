package com.khalidtouch.classifiadmin.data.mapper

import com.khalidtouch.chatme.database.models.ClassifiAcademicSessionEntity
import com.khalidtouch.chatme.database.models.ClassifiClassEntity
import com.khalidtouch.chatme.database.models.ClassifiCommentEntity
import com.khalidtouch.chatme.database.models.ClassifiFeedEntity
import com.khalidtouch.chatme.database.models.ClassifiLikeEntity
import com.khalidtouch.chatme.database.models.ClassifiMessageEntity
import com.khalidtouch.chatme.database.models.ClassifiSchoolEntity
import com.khalidtouch.chatme.database.models.ClassifiUserEntity
import com.khalidtouch.chatme.database.models.RUserAccount
import com.khalidtouch.chatme.database.models.RUserContact
import com.khalidtouch.chatme.database.models.RUserProfile
import com.khalidtouch.chatme.database.relations.UserWithSchools
import com.khalidtouch.classifiadmin.model.MessageType
import com.khalidtouch.classifiadmin.model.UserAccount
import com.khalidtouch.classifiadmin.model.UserContact
import com.khalidtouch.classifiadmin.model.UserProfile
import com.khalidtouch.classifiadmin.model.UserRole
import com.khalidtouch.classifiadmin.model.classifi.ClassifiAcademicSession
import com.khalidtouch.classifiadmin.model.classifi.ClassifiClass
import com.khalidtouch.classifiadmin.model.classifi.ClassifiComment
import com.khalidtouch.classifiadmin.model.classifi.ClassifiFeed
import com.khalidtouch.classifiadmin.model.classifi.ClassifiLike
import com.khalidtouch.classifiadmin.model.classifi.ClassifiMessage
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import java.time.LocalDateTime
import javax.inject.Inject


class ModelEntityMapperImpl @Inject constructor() : ModelEntityMapper {
    override fun classEntityToModel(classifiClass: ClassifiClassEntity?): ClassifiClass? {
        return ClassifiClass(
            classId = classifiClass?.classId.orEmpty(),
            className = classifiClass?.className.orEmpty(),
            classCode = classifiClass?.classCode.orEmpty(),
            formTeacherId = classifiClass?.formTeacherId.orEmpty(),
            prefectId = classifiClass?.prefectId.orEmpty(),
            dateCreated = classifiClass?.dateCreated ?: LocalDateTime.now(),
            creatorId = classifiClass?.creatorId.orEmpty(),
            feeds = emptyList(),
            teachers = emptyList(),
            students = emptyList(),
            schoolId = classifiClass?.schoolId.orEmpty(),
        )
    }

    override fun classModelToEntity(classifiClass: ClassifiClass?): ClassifiClassEntity? {
        return ClassifiClassEntity(
            classId = classifiClass?.classId.orEmpty(),
            className = classifiClass?.className.orEmpty(),
            classCode = classifiClass?.classCode.orEmpty(),
            formTeacherId = classifiClass?.formTeacherId.orEmpty(),
            prefectId = classifiClass?.prefectId.orEmpty(),
            dateCreated = classifiClass?.dateCreated ?: LocalDateTime.now(),
            creatorId = classifiClass?.creatorId.orEmpty(),
            schoolId = classifiClass?.schoolId.orEmpty(),
        )
    }

    override fun sessionEntityToModel(session: ClassifiAcademicSessionEntity?): ClassifiAcademicSession? {
        return ClassifiAcademicSession(
            sessionId = session?.sessionId.orEmpty(),
            sessionName = session?.sessionName.orEmpty(),
            startDate = session?.startDate ?: LocalDateTime.now(),
            endDate = session?.endDate ?: LocalDateTime.now(),
            schoolId = session?.schoolId.orEmpty()
        )
    }

    override fun sessionModelToEntity(session: ClassifiAcademicSession?): ClassifiAcademicSessionEntity? {
        return ClassifiAcademicSessionEntity(
            sessionId = session?.sessionId.orEmpty(),
            schoolId = session?.schoolId.orEmpty(),
            sessionName = session?.sessionName.orEmpty(),
            startDate = session?.startDate ?: LocalDateTime.now(),
            endDate = session?.endDate ?: LocalDateTime.now(),
        )
    }

    override fun commentEntityToModel(comment: ClassifiCommentEntity?): ClassifiComment? {
        return ClassifiComment(
            commentId = comment?.commentId.orEmpty(),
            messages = emptyList(),
            likes = emptyList(),
            feedId = comment?.feedId.orEmpty(),
            dateCreated = comment?.dateCreated ?: LocalDateTime.now(),
            userId = comment?.userId.orEmpty(),
        )
    }

    override fun commentModelToEntity(comment: ClassifiComment?): ClassifiCommentEntity? {
        return ClassifiCommentEntity(
            commentId = comment?.commentId.orEmpty(),
            feedId = comment?.feedId.orEmpty(),
            dateCreated = comment?.dateCreated ?: LocalDateTime.now(),
            userId = comment?.userId.orEmpty(),
        )
    }

    override fun feedEntityToModel(feed: ClassifiFeedEntity?): ClassifiFeed? {
        return ClassifiFeed(
            feedId = feed?.feedId.orEmpty(),
            creator = ClassifiUser(
                userId = feed?.creatorId.orEmpty(),
            ),
            messages = emptyList(),
            likes = emptyList(),
            comments = emptyList(),
            classes = emptyList(),
            dateCreated = feed?.dateCreated ?: LocalDateTime.now()
        )
    }

    override fun feedModelToEntity(feed: ClassifiFeed?): ClassifiFeedEntity? {
        return ClassifiFeedEntity(
            feedId = feed?.feedId.orEmpty(),
            creatorId = feed?.creator?.userId,
            dateCreated = feed?.dateCreated ?: LocalDateTime.now()
        )
    }

    override fun likeEntityToModel(like: ClassifiLikeEntity?): ClassifiLike? {
        return ClassifiLike(
            likeId = like?.likeId.orEmpty(),
            feedId = like?.feedId.orEmpty(),
            userId = like?.feedId.orEmpty(),
        )
    }

    override fun likeModelToEntity(like: ClassifiLike?): ClassifiLikeEntity? {
        return ClassifiLikeEntity(
            likeId = like?.likeId.orEmpty(),
            feedId = like?.feedId.orEmpty(),
            userId = like?.feedId.orEmpty(),
        )
    }

    override fun messageEntityToModel(message: ClassifiMessageEntity?): ClassifiMessage? {
        return ClassifiMessage(
            messageId = message?.messageId.orEmpty(),
            type = message?.type ?: MessageType.Unknown,
            message = message?.message.orEmpty(),
            truncatedMessage = message?.truncatedMessage.orEmpty(),
            dateCreated = message?.dateCreated ?: LocalDateTime.now(),
            feedId = message?.feedId.orEmpty(),
            commentId = message?.commentId.orEmpty(),
        )
    }

    override fun messageModelToEntity(message: ClassifiMessage?): ClassifiMessageEntity? {
        return ClassifiMessageEntity(
            messageId = message?.messageId.orEmpty(),
            type = message?.type ?: MessageType.Unknown,
            message = message?.message.orEmpty(),
            truncatedMessage = message?.truncatedMessage.orEmpty(),
            dateCreated = message?.dateCreated ?: LocalDateTime.now(),
            feedId = message?.feedId.orEmpty(),
            commentId = message?.commentId.orEmpty(),
        )
    }

    override fun schoolEntityToModel(school: ClassifiSchoolEntity?): ClassifiSchool? {
        return ClassifiSchool(
            schoolId = school?.schoolId.orEmpty(),
            schoolName = school?.schoolName.orEmpty(),
            address = school?.address.orEmpty(),
            description = school?.description.orEmpty(),
            bannerImage = school?.bannerImage.orEmpty(),
            dateCreated = school?.dateCreated ?: LocalDateTime.now(),
            students = emptyList(),
            teachers = emptyList(),
            parents = emptyList(),
            classes = emptyList(),
            sessions = emptyList(),
        )
    }

    override fun schoolModelToEntity(school: ClassifiSchool?): ClassifiSchoolEntity? {
        return ClassifiSchoolEntity(
            schoolId = school?.schoolId.orEmpty(),
            schoolName = school?.schoolName.orEmpty(),
            address = school?.address.orEmpty(),
            description = school?.description.orEmpty(),
            bannerImage = school?.bannerImage.orEmpty(),
            dateCreated = school?.dateCreated ?: LocalDateTime.now(),
        )
    }

    override fun userEntityToModel(user: ClassifiUserEntity?): ClassifiUser? {
        return ClassifiUser(
            userId = user?.userId.orEmpty(),
            account = user?.account?.toModel(),
            profile = user?.profile?.toModel(),
            dateCreated = user?.dateCreated ?: LocalDateTime.now(),
            joinedSchools = emptyList(),
            joinedClasses = emptyList(),
        )
    }

    override fun userEntityToModel2(user: UserWithSchools?): ClassifiUser? {
        return ClassifiUser(
            userId = user?.user?.userId,
            account = user?.user?.account?.toModel(),
            profile = user?.user?.profile?.toModel(),
            dateCreated = user?.user?.dateCreated,
            joinedSchools = user?.schools?.map { schoolEntityToModel(it)!! } ?: listOf(),
            joinedClasses = listOf()
        )
    }

    override fun userModelToEntity(user: ClassifiUser?): ClassifiUserEntity? {
        return ClassifiUserEntity(
            userId = user?.userId.orEmpty(),
            account = user?.account?.toRoom(),
            profile = user?.profile?.toRoom(),
            dateCreated = user?.dateCreated ?: LocalDateTime.now(),
        )
    }

    override fun userModelToEntity2(user: ClassifiUser?): UserWithSchools? {
        return UserWithSchools(
            user = userModelToEntity(user)!!,
            schools = user?.joinedSchools?.map { schoolModelToEntity(it)!! } ?: listOf()
        )
    }
}


fun UserContact.toRoom() = RUserContact(
    address = address,
    country = country,
    stateOfCountry = stateOfCountry,
    city = city,
    postalCode = postalCode
)

fun RUserContact.toModel() = UserContact(
    address = address,
    country = country,
    stateOfCountry = stateOfCountry,
    city = city,
    postalCode = postalCode
)

fun UserAccount.toRoom() = RUserAccount(
    username = username,
    email = email,
    userRole = userRole ?: UserRole.Guest
)

fun RUserAccount.toModel() = UserAccount(
    username = username.orEmpty(),
    email = email.orEmpty(),
    userRole = userRole ?: UserRole.Guest,
)

fun UserProfile.toRoom() = RUserProfile(
    profileImage = profileImage,
    phone = phone,
    bio = bio,
    dob = dob,
    contact = contact?.toRoom(),
)

fun RUserProfile.toModel() = UserProfile(
    profileImage = profileImage,
    phone = phone,
    bio = bio,
    dob = dob,
    contact = contact?.toModel(),
)


fun Long?.orEmpty(): Long = this ?: -1L