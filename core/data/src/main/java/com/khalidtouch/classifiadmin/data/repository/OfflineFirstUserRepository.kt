package com.khalidtouch.classifiadmin.data.repository

import android.content.Context
import androidx.paging.PagingData
import com.khalidtouch.chatme.database.dao.UserDao
import com.khalidtouch.chatme.database.relations.UsersWithSchoolsCrossRef
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.chatme.network.UserNetworkDataSource
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.data.mapper.orEmpty
import com.khalidtouch.classifiadmin.data.mapper.toModel
import com.khalidtouch.classifiadmin.data.pagingsources.UserPagingSource
import com.khalidtouch.classifiadmin.data.util.ReadCountriesUseCase
import com.khalidtouch.classifiadmin.model.PagedCountry
import com.khalidtouch.classifiadmin.model.UserRole
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstUserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDao: UserDao,
    private val modelMapper: ModelEntityMapper,
    private val readCountriesUseCase: ReadCountriesUseCase,
    private val userDataRepository: UserDataRepository,
    private val userNetworkDataSource: UserNetworkDataSource,
    private val userPagingSource: UserPagingSource,
) : UserRepository {
    override suspend fun saveUser(user: ClassifiUser) {
        userDao.saveUserOrIgnore(modelMapper.userModelToEntity(user)!!)
        userDataRepository.setUserId(user.userId ?: -1)
        userDataRepository.setUserEmail(user.account?.email.orEmpty())
        userDataRepository.setUserRole(user.account?.userRole ?: UserRole.Guest)
        userNetworkDataSource.saveUser(user)
    }

    override suspend fun saveUsers(users: List<ClassifiUser>) {
        userDao.saveUsersOrIgnore(
            users.map { user -> modelMapper.userModelToEntity(user)!! }
        )
        users.map {
            userNetworkDataSource.saveUser(it)
        }
    }

    override suspend fun registerUserWithSchool(userId: Long, schoolId: Long, schoolName: String) {
        userDao.registerUserWithSchool(UsersWithSchoolsCrossRef(userId, schoolId))
        userNetworkDataSource.registerUserWithSchool(userId, schoolId, schoolName)
    }

    override suspend fun updateUser(user: ClassifiUser) {
        userDao.updateUser(
            modelMapper.userModelToEntity(user)!!
        )
        userDataRepository.setUserId(user.userId ?: -1)
        userDataRepository.setUserRole(user.account?.userRole ?: UserRole.Guest)
        userNetworkDataSource.updateUser(user)
    }

    override suspend fun updateUsers(users: List<ClassifiUser>) {
        userDao.updateUsers(
            users.map { user -> modelMapper.userModelToEntity(user)!! }
        )
        users.map { userNetworkDataSource.updateUser(it) }
    }

    override suspend fun deleteUser(user: ClassifiUser) {
        userDao.deleteUser(modelMapper.userModelToEntity(user)!!)
    }

    override suspend fun deleteUsers(ids: List<Long>) {
        userDao.deleteUsers(ids)
    }

    override suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    override suspend fun fetchUserById(userId: Long): ClassifiUser? {
        return modelMapper.userEntityToModel(userDao.fetchUserById(userId))
    }

    override fun observeUserById(userId: Long): Flow<ClassifiUser?> {
        return userDao.observeUserWithId(userId).map {
            modelMapper.userEntityToModel(it)
        }
    }

    override suspend fun fetchUserByEmail(email: String): ClassifiUser? {
        return modelMapper.userEntityToModel(userDao.fetchUserByEmail(email))
    }

    override fun observeAllUsers(): Flow<List<ClassifiUser>> {
        return userDao.observeAllUsers().map {
            it.map { user -> modelMapper.userEntityToModel(user)!! }
        }
    }


    override suspend fun fetchAllUsersList(): List<ClassifiUser> {
        return userDao.fetchAllUsers().map { modelMapper.userEntityToModel(it)!! }
    }

    override suspend fun fetchUserWithSchools(userId: Long): ClassifiUser? {
        val userWithSchools = userDao.fetchUserWithSchools(userId)
        return ClassifiUser(
            userId = userWithSchools?.user?.userId.orEmpty(),
            account = userWithSchools?.user?.account?.toModel(),
            profile = userWithSchools?.user?.profile?.toModel(),
            dateCreated = userWithSchools?.user?.dateCreated,
            joinedSchools = userWithSchools?.schools?.map { modelMapper.schoolEntityToModel(it)!! }
                ?: emptyList(),
        )

    }

    override suspend fun fetchUserWithClasses(userId: Long): ClassifiUser? {
        val userWithClasses = userDao.fetchUserWithClasses(userId)
        return ClassifiUser(
            userId = userWithClasses?.user?.userId.orEmpty(),
            account = userWithClasses?.user?.account?.toModel(),
            profile = userWithClasses?.user?.profile?.toModel(),
            dateCreated = userWithClasses?.user?.dateCreated,
            joinedClasses = userWithClasses?.classes?.map { modelMapper.classEntityToModel(it)!! }
                ?: emptyList(),
        )
    }

    override suspend fun getCountriesFromJson(page: Int, limit: Int): PagedCountry {
        return readCountriesUseCase(context, page, limit)
    }

    override fun observeTeachersFromMySchool(pageSize: Int): Flow<PagingData<ClassifiUser>> {
        return userPagingSource.observeTeachersFromMySchoolAsPaged(pageSize)
    }
}