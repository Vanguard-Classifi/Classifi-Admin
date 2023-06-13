package com.khalidtouch.chatme.network

import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser

interface UserNetworkDataSource {
    fun saveUser(user: ClassifiUser)

    fun updateUser(user: ClassifiUser)

    fun registerUserWithSchool(userId: Long, schoolId: Long)

    fun unregisterUserWithSchool(userId: Long, schoolId: Long,)

    fun fetchUserById(userId: Long, callback: (ClassifiUser?) -> Unit)

    fun fetchUserByEmail(email: String, callback: (ClassifiUser?) -> Unit)

    fun fetchAllUsersList(schoolId: Long, callback: (List<ClassifiUser>) -> Unit)

}