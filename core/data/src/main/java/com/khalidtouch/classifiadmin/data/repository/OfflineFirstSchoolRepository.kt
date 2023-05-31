package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.database.dao.SchoolDao
import com.khalidtouch.chatme.database.relations.UsersWithSchoolsCrossRef
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.data.mapper.orEmpty
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import java.time.LocalDateTime
import javax.inject.Inject


class OfflineFirstSchoolRepository @Inject constructor(
    private val modelMapper: ModelEntityMapper,
    private val schoolDao: SchoolDao,
) : SchoolRepository {
    override suspend fun saveSchool(school: ClassifiSchool) {
        schoolDao.saveSchoolOrIgnore(modelMapper.schoolModelToEntity(school)!!)
    }

    override suspend fun saveSchools(schools: List<ClassifiSchool>) {
        schoolDao.saveSchoolsOrIgnore(schools.map { school -> modelMapper.schoolModelToEntity(school)!! })
    }

    override suspend fun registerUserWithSchool(userId: Long, schoolId: Long) {
        schoolDao.registerUserWithSchool(UsersWithSchoolsCrossRef(userId, schoolId))
    }

    override suspend fun updateSchool(school: ClassifiSchool) {
        schoolDao.updateSchool(modelMapper.schoolModelToEntity(school)!!)
    }

    override suspend fun deleteSchool(school: ClassifiSchool) {
        schoolDao.deleteSchool(modelMapper.schoolModelToEntity(school)!!)
    }

    override suspend fun fetchSchoolById(schoolId: Long): ClassifiSchool? {
        val school = schoolDao.fetchSchoolById(schoolId)
        return modelMapper.schoolEntityToModel(school)
    }

    override suspend fun fetchSchoolWithSessions(schoolId: Long): ClassifiSchool? {
        val schoolWithSessions = schoolDao.fetchSchoolWithSessions(schoolId)
        return ClassifiSchool(
            schoolId = schoolWithSessions?.school?.schoolId.orEmpty(),
            schoolName = schoolWithSessions?.school?.schoolName.orEmpty(),
            address = schoolWithSessions?.school?.address.orEmpty(),
            description = schoolWithSessions?.school?.description.orEmpty(),
            bannerImage = schoolWithSessions?.school?.bannerImage.orEmpty(),
            dateCreated = schoolWithSessions?.school?.dateCreated ?: LocalDateTime.now(),
            sessions = schoolWithSessions?.sessions?.map { session ->
                modelMapper.sessionEntityToModel(
                    session
                )!!
            } ?: emptyList(),
            students = emptyList(),
            teachers = emptyList(),
            parents = emptyList(),
            classes = emptyList(),
        )
    }

    override suspend fun fetchSchoolWithClasses(schoolId: Long): ClassifiSchool? {
        val schoolWithClasses = schoolDao.fetchSchoolWithClasses(schoolId)
        return ClassifiSchool(
            schoolId = schoolWithClasses?.school?.schoolId.orEmpty(),
            schoolName = schoolWithClasses?.school?.schoolName.orEmpty(),
            address = schoolWithClasses?.school?.address.orEmpty(),
            description = schoolWithClasses?.school?.description.orEmpty(),
            bannerImage = schoolWithClasses?.school?.bannerImage.orEmpty(),
            dateCreated = schoolWithClasses?.school?.dateCreated ?: LocalDateTime.now(),
            students = emptyList(),
            teachers = emptyList(),
            parents = emptyList(),
            classes = schoolWithClasses?.classes?.map { modelMapper.classEntityToModel(it)!! }
                ?: emptyList(),
            sessions = emptyList(),
        )
    }

    override suspend fun fetchSchoolWithStudents(schoolId: Long): ClassifiSchool? {
        val schoolWithStudents = schoolDao.fetchSchoolWithUsers(schoolId)
        return ClassifiSchool(
            schoolId = schoolWithStudents?.school?.schoolId.orEmpty(),
            schoolName = schoolWithStudents?.school?.schoolName.orEmpty(),
            address = schoolWithStudents?.school?.address.orEmpty(),
            description = schoolWithStudents?.school?.description.orEmpty(),
            bannerImage = schoolWithStudents?.school?.bannerImage.orEmpty(),
            dateCreated = schoolWithStudents?.school?.dateCreated ?: LocalDateTime.now(),
            students = schoolWithStudents?.users?.map { student ->
                modelMapper.userEntityToModel(
                    student
                )!!
            }
                ?.filter { student -> student.isAStudent } ?: emptyList(),
            teachers = emptyList(),
            parents = emptyList(),
            classes = emptyList(),
            sessions = emptyList(),
        )
    }

    override suspend fun fetchSchoolWithTeachers(schoolId: Long): ClassifiSchool? {
        val schoolWithTeachers = schoolDao.fetchSchoolWithUsers(schoolId)
        return ClassifiSchool(
            schoolId = schoolWithTeachers?.school?.schoolId.orEmpty(),
            schoolName = schoolWithTeachers?.school?.schoolName.orEmpty(),
            address = schoolWithTeachers?.school?.address.orEmpty(),
            description = schoolWithTeachers?.school?.description.orEmpty(),
            bannerImage = schoolWithTeachers?.school?.bannerImage.orEmpty(),
            dateCreated = schoolWithTeachers?.school?.dateCreated ?: LocalDateTime.now(),
            teachers = schoolWithTeachers?.users?.map { teacher ->
                modelMapper.userEntityToModel(
                    teacher
                )!!
            }
                ?.filter { teacher -> teacher.isATeacher } ?: emptyList(),
            students = emptyList(),
            parents = emptyList(),
            classes = emptyList(),
            sessions = emptyList(),
        )
    }

    override suspend fun fetchSchoolWithParents(schoolId: Long): ClassifiSchool? {
        val schoolWithParents = schoolDao.fetchSchoolWithUsers(schoolId)
        return ClassifiSchool(
            schoolId = schoolWithParents?.school?.schoolId.orEmpty(),
            schoolName = schoolWithParents?.school?.schoolName.orEmpty(),
            address = schoolWithParents?.school?.address.orEmpty(),
            description = schoolWithParents?.school?.description.orEmpty(),
            bannerImage = schoolWithParents?.school?.bannerImage.orEmpty(),
            dateCreated = schoolWithParents?.school?.dateCreated ?: LocalDateTime.now(),
            parents = schoolWithParents?.users?.map { parent ->
                modelMapper.userEntityToModel(
                    parent
                )!!
            }
                ?.filter { parent -> parent.isAParent } ?: emptyList(),
            students = emptyList(),
            teachers = emptyList(),
            classes = emptyList(),
            sessions = emptyList(),
        )
    }

}