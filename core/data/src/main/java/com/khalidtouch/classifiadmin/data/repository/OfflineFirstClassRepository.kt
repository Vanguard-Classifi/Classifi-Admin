package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.database.dao.ClassDao
import com.khalidtouch.chatme.domain.repository.ClassRepository
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.data.mapper.orEmpty
import com.khalidtouch.classifiadmin.model.classifi.ClassifiClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class OfflineFirstClassRepository @Inject constructor(
    private val modelMapper: ModelEntityMapper,
    private val classDao: ClassDao,
) : ClassRepository {
    override suspend fun saveClass(classifiClass: ClassifiClass) {
        classDao.saveClassOrIgnore(modelMapper.classModelToEntity(classifiClass)!!)
    }

    override suspend fun saveClasses(classes: List<ClassifiClass>) {
        classDao.saveClassesOrIgnore(classes.map { cs -> modelMapper.classModelToEntity(cs)!! })
    }

    override suspend fun updateClass(classifiClass: ClassifiClass) {
        classDao.updateClass(modelMapper.classModelToEntity(classifiClass)!!)
    }

    override suspend fun deleteClass(classifiClass: ClassifiClass) {
        classDao.deleteClass(modelMapper.classModelToEntity(classifiClass)!!)
    }

    override fun fetchClassById(classId: Long): Flow<ClassifiClass?> = flow {
        classDao.fetchClassById(classId).collect {
            val c = modelMapper.classEntityToModel(it)
            emit(c)
        }
    }

    override fun fetchAllClassesBySchool(schoolId: Long): Flow<List<ClassifiClass>> = flow {
        classDao.fetchAllClassesBySchool(schoolId).collect {
            val cs = it.map { c -> modelMapper.classEntityToModel(c)!! }
            emit(cs)
        }
    }

    override fun fetchClassWithFeeds(classId: Long): Flow<ClassifiClass?> = flow {
        classDao.fetchClassWithFeeds(classId).collect {
            emit(
                ClassifiClass(
                    classId = it?.classifiClass?.classId.orEmpty(),
                    schoolId = it?.classifiClass?.schoolId.orEmpty(),
                    className = it?.classifiClass?.className.orEmpty(),
                    classCode = it?.classifiClass?.classCode.orEmpty(),
                    formTeacherId = it?.classifiClass?.formTeacherId.orEmpty(),
                    prefectId = it?.classifiClass?.prefectId.orEmpty(),
                    dateCreated = it?.classifiClass?.dateCreated ?: LocalDateTime.now(),
                    creatorId = it?.classifiClass?.creatorId.orEmpty(),
                    feeds = it?.feeds?.map { feed -> modelMapper.feedEntityToModel(feed)!! }
                        ?: emptyList(),
                    teachers = emptyList(),
                    students = emptyList(),
                )
            )
        }
    }

    override fun fetchClassWithTeachers(classId: Long): Flow<ClassifiClass?> = flow {
        classDao.fetchClassWithTeachers(classId).collect {
            emit(
                ClassifiClass(
                    classId = it?.classifiClass?.classId.orEmpty(),
                    schoolId = it?.classifiClass?.schoolId.orEmpty(),
                    className = it?.classifiClass?.className.orEmpty(),
                    classCode = it?.classifiClass?.classCode.orEmpty(),
                    formTeacherId = it?.classifiClass?.formTeacherId.orEmpty(),
                    prefectId = it?.classifiClass?.prefectId.orEmpty(),
                    dateCreated = it?.classifiClass?.dateCreated ?: LocalDateTime.now(),
                    creatorId = it?.classifiClass?.creatorId.orEmpty(),
                    teachers = it?.users?.map { teacher -> modelMapper.userEntityToModel(teacher)!! }
                        ?.filter { teacher -> teacher.isATeacher }
                        ?: emptyList(),
                    feeds = emptyList(),
                    students = emptyList(),
                )
            )
        }
    }

    override fun fetchClassWithStudents(classId: Long): Flow<ClassifiClass?> = flow {
        classDao.fetchClassWithStudents(classId).collect {
            emit(
                ClassifiClass(
                    classId = it?.classifiClass?.classId.orEmpty(),
                    schoolId = it?.classifiClass?.schoolId.orEmpty(),
                    className = it?.classifiClass?.className.orEmpty(),
                    classCode = it?.classifiClass?.classCode.orEmpty(),
                    formTeacherId = it?.classifiClass?.formTeacherId.orEmpty(),
                    prefectId = it?.classifiClass?.prefectId.orEmpty(),
                    dateCreated = it?.classifiClass?.dateCreated ?: LocalDateTime.now(),
                    creatorId = it?.classifiClass?.creatorId.orEmpty(),
                    students = it?.users?.map { student -> modelMapper.userEntityToModel(student)!! }
                        ?.filter { student -> student.isAStudent }
                        ?: emptyList(),
                    feeds = emptyList(),
                    teachers = emptyList(),
                )
            )
        }
    }

}