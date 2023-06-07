package com.khalidtouch.chatme.network

import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.classifiadmin.model.utils.CreateAccountData
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StageTeacher

interface UserNetworkDataSource {
    fun saveUser(user: ClassifiUser)

    fun updateUser(user: ClassifiUser)

    fun registerUserWithSchool(userId: Long, schoolId: Long, schoolName: String)

    fun deleteUser(user: ClassifiUser)

    fun fetchUserById(userId: Long, callback: (ClassifiUser?) -> Unit)

    fun fetchUserByEmail(email: String, callback: (ClassifiUser?) -> Unit)

    fun fetchAllUsersList(schoolId: Long, callback: (List<ClassifiUser>) -> Unit)

}