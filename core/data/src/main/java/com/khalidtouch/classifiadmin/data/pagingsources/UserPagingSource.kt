package com.khalidtouch.classifiadmin.data.pagingsources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.khalidtouch.chatme.database.dao.UserDao
import com.khalidtouch.chatme.database.relations.UserWithSchools
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val userDao: UserDao,
    private val modelEntityMapper: ModelEntityMapper
) {
    fun observeTeachersFromMySchoolAsPaged(pageSize: Int): Flow<PagingData<ClassifiUser>> =
        userFromSchools(pageSize) { userDao.observeTeachersFromMySchoolAsPaged() }


    private fun userFromSchools(
        pageSize: Int,
        block: () -> PagingSource<Int, UserWithSchools>
    ): Flow<PagingData<ClassifiUser>> =
        Pager(PagingConfig(pageSize)) { block() }.flow
            .map { page -> page.map { user -> modelEntityMapper.userEntityToModel2(user)!! } }
}