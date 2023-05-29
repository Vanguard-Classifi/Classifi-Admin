package com.khalidtouch.chatme.domain.repository

import com.khalidtouch.classifiadmin.model.PagedCountry
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: ClassifiUser)

    suspend fun saveUsers(users: List<ClassifiUser>)

    suspend fun updateUser(user: ClassifiUser)

    suspend fun updateUsers(users: List<ClassifiUser>)

    suspend fun deleteUser(user: ClassifiUser)

    suspend fun deleteUsers(ids: List<Long>)

    suspend fun deleteAllUsers()

    suspend fun fetchUserById(userId: Long): ClassifiUser?

    suspend fun fetchUserByEmail(email: String): ClassifiUser?

    fun fetchAllUsers(): Flow<List<ClassifiUser>>

    suspend fun fetchAllUsersList(): List<ClassifiUser>

    suspend fun fetchUserWithSchools(userId: Long): ClassifiUser?

    suspend fun fetchUserWithClasses(userId: Long): ClassifiUser?

    suspend fun getCountriesFromJson(page: Int, limit: Int): PagedCountry
}