package com.khalidtouch.chatme.network

import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser

interface UserNetworkDataSource {
    fun saveUser(user: ClassifiUser)

    fun updateUser(user: ClassifiUser)

    fun registerUserWithSchool(userId: Long, schoolId: Long, schoolName: String)

    fun deleteUser(user: ClassifiUser)

    fun fetchUserById(userId: Long, callback: (ClassifiUser?) -> Unit)

    fun fetchUserByEmail(email: String, callback: (ClassifiUser?) -> Unit)

    fun fetchAllUsersList(schoolId: Long, callback: (List<ClassifiUser>) -> Unit)

}