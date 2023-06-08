package com.khalidtouch.chatme.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.khalidtouch.chatme.database.models.ClassifiUserEntity
import com.khalidtouch.chatme.database.relations.UserWithClasses
import com.khalidtouch.chatme.database.relations.UserWithSchools
import com.khalidtouch.chatme.database.relations.UsersWithSchoolsCrossRef
import com.khalidtouch.classifiadmin.model.UserRole
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveUserOrIgnore(user: ClassifiUserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveUsersOrIgnore(users: List<ClassifiUserEntity>)

    @Insert
    suspend fun registerUserWithSchool(userWithSchool: UsersWithSchoolsCrossRef)

    @Update
    suspend fun updateUser(user: ClassifiUserEntity)

    @Upsert
    suspend fun updateUsers(users: List<ClassifiUserEntity>)

    @Delete
    suspend fun deleteUser(user: ClassifiUserEntity)

    @Query("delete from ClassifiUserEntity")
    suspend fun deleteAllUsers()

    @Query(
        value = "delete from ClassifiUserEntity where userId in (:ids)"
    )
    suspend fun deleteUsers(ids: List<Long>)

    @Query(
        value = "select * from ClassifiUserEntity where userId = :id"
    )
    suspend fun fetchUserById(id: Long): ClassifiUserEntity?

    @Query(
        value = "select * from ClassifiUserEntity where userId = :id"
    )
    fun observeUserWithId(id: Long): Flow<ClassifiUserEntity?>


    @Query(
        value = "select * from ClassifiUserEntity where email = :email"
    )
    suspend fun fetchUserByEmail(email: String): ClassifiUserEntity?


    @Query(
        value = "select * from ClassifiUserEntity where username = :username"
    )
    suspend fun fetchUserByUsername(username: String): ClassifiUserEntity?

    @Query(
        value = "select * from ClassifiUserEntity order by username asc"
    )
    fun observeAllUsers(): Flow<List<ClassifiUserEntity>>

    @Query(
        value = "select * from ClassifiUserEntity order by username asc"
    )
    suspend fun fetchAllUsers(): List<ClassifiUserEntity>

    @Query(
        value = "select * from ClassifiUserEntity where userRole like :role order by username asc"
    )
    @Transaction
    fun observeTeachersFromMySchoolAsPaged(role: UserRole = UserRole.Teacher): PagingSource<Int, UserWithSchools>

    @Query(
        value = "select * from ClassifiUserEntity where userId = :userId"
    )
    @Transaction
    suspend fun fetchUserWithSchools(userId: Long): UserWithSchools?

    @Query(
        value = "select * from ClassifiUserEntity where userId = :userId"
    )
    @Transaction
    suspend fun fetchUserWithClasses(userId: Long): UserWithClasses?
}