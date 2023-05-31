package com.vanguard.classifiadmin.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khalidtouch.chatme.network.UserNetworkDataSource
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.core.common.firestore.ClassifiStore
import javax.inject.Inject


class UserNetworkDataSourceImpl @Inject constructor(): UserNetworkDataSource {
    private val fireStore: FirebaseFirestore = Firebase.firestore

    override fun saveUser(user: ClassifiUser) {
       fireStore.collection(ClassifiStore.USERS).document(user.account?.email.orEmpty())
           .set(user)
           .addOnSuccessListener {  }
           .addOnFailureListener {  }
    }

    override fun updateUser(user: ClassifiUser) {
        fireStore.collection(ClassifiStore.USERS).document(user.account?.email.orEmpty())
            .set(user)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

    override fun registerUserWithSchool(userId: Long, schoolId: Long, schoolName: String) {
        fireStore
            .collection(ClassifiStore.SCHOOL)
            .document(schoolName)
            .collection(ClassifiStore.REGISTRY).document(ClassifiStore.Namespace.UserWithSchool)
            .set(hashMapOf<Long, Long>(userId to schoolId))
    }

    override fun deleteUser(user: ClassifiUser) {
    }

    override fun fetchUserById(userId: Long, callback: (ClassifiUser?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun fetchUserByEmail(email: String, callback: (ClassifiUser?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun fetchAllUsersList(schoolId: Long, callback: (List<ClassifiUser>) -> Unit) {
        TODO("Not yet implemented")
    }
}