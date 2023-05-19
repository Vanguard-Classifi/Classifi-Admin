package com.khalidtouch.classifiadmin.data.repository

import com.khalidtouch.chatme.database.dao.UserDao
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.data.mapper.ModelEntityMapper
import com.khalidtouch.classifiadmin.data.mapper.orEmpty
import com.khalidtouch.classifiadmin.data.mapper.toModel
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OfflineFirstUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val modelMapper: ModelEntityMapper,
) : UserRepository {
    override suspend fun saveUser(user: ClassifiUser) {
        userDao.saveUserOrIgnore(modelMapper.userModelToEntity(user)!!)
        /*todo -> then, save to server */
    }

    override suspend fun saveUsers(users: List<ClassifiUser>) {
        userDao.saveUsersOrIgnore(
            users.map { user -> modelMapper.userModelToEntity(user)!! }
        )
        /*todo -> then, save to server */
    }

    override suspend fun updateUser(user: ClassifiUser) {
        userDao.updateUser(
            modelMapper.userModelToEntity(user)!!
        )
    }

    override suspend fun updateUsers(users: List<ClassifiUser>) {
        userDao.updateUsers(
            users.map { user -> modelMapper.userModelToEntity(user)!! }
        )
    }

    override suspend fun deleteUser(user: ClassifiUser) {
        userDao.deleteUser(modelMapper.userModelToEntity(user)!!)
    }

    override suspend fun deleteUsers(ids: List<Long>) {
        userDao.deleteUsers(ids)
    }

    override suspend fun fetchUserById(userId: Long): ClassifiUser? {
        return modelMapper.userEntityToModel(userDao.fetchUserById(userId))
    }

    override suspend fun fetchUserByEmail(email: String): ClassifiUser? {
        return modelMapper.userEntityToModel(userDao.fetchUserByEmail(email))
    }

    override fun fetchAllUsers(): Flow<List<ClassifiUser>> = flow {
        val flowOfUsers = userDao.fetchAllUsers()
        flowOfUsers.collect {
            emit(it.map { user -> modelMapper.userEntityToModel(user)!! })
        }
    }

    override suspend fun fetchAllUsersList(): List<ClassifiUser> {
        return userDao.fetchAllUsersList().map { modelMapper.userEntityToModel(it)!! }
    }

    override suspend fun fetchUserWithSchools(userId: Long): ClassifiUser? {
        val userWithSchools = userDao.fetchUserWithSchools(userId)
        return ClassifiUser(
            userId = userWithSchools?.user?.userId.orEmpty(),
            account = userWithSchools?.user?.account?.toModel(),
            profile = userWithSchools?.user?.profile?.toModel(),
            dateCreated = userWithSchools?.user?.dateCreated,
            joinedSchools = userWithSchools?.schools ?: emptyList(),
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
}