package com.khalidtouch.classifiadmin.data.pagingsources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.filter
import androidx.paging.map
import com.khalidtouch.chatme.database.dao.UserDao
import com.khalidtouch.chatme.database.relations.UserWithSchools
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.model.UserRole
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val userDao: UserDao,
    private val modelEntityMapper: ModelEntityMapper
) {
    fun observeTeachersFromMySchoolAsPaged(pageSize: Int, schoolId: Long, role: UserRole = UserRole.Teacher): Flow<PagingData<ClassifiUser>> =
        userFromSchools(pageSize, schoolId) { userDao.observeUsersFromMySchoolAsPaged(role) }

    fun observeParentsFromMySchoolAsPaged(pageSize: Int, schoolId: Long, role: UserRole = UserRole.Parent): Flow<PagingData<ClassifiUser>> =
        userFromSchools(pageSize, schoolId) { userDao.observeUsersFromMySchoolAsPaged(role) }


    private fun userFromSchools(
        pageSize: Int,
        schoolId: Long,
        block: () -> PagingSource<Int, UserWithSchools>
    ): Flow<PagingData<ClassifiUser>> =
        Pager(PagingConfig(pageSize)) { block() }.flow
            .map { page -> page.map { user -> modelEntityMapper.userEntityToModel2(user)!! } }
            .mapNotNull { page -> page.filter { model -> model.joinedSchools.any { school -> school.schoolId == schoolId } } }
}