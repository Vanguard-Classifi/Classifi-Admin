package com.vanguard.classifiadmin.network

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khalidtouch.chatme.network.UserNetworkDataSource
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUserSchoolCrossRef
import com.khalidtouch.core.common.firestore.ClassifiStore
import javax.inject.Inject


class UserNetworkDataSourceImpl @Inject constructor(): UserNetworkDataSource {
    private val fireStore: FirebaseFirestore = Firebase.firestore
    private val authentication: FirebaseAuth = Firebase.auth
    val TAG = "UserNetwork"

    override fun saveUser(user: ClassifiUser) {
       fireStore.collection(ClassifiStore.USERS).document(user.account?.email.orEmpty())
           .set(user)
           .addOnSuccessListener {  }
           .addOnFailureListener {  }
    }

    override fun updateUser(user: ClassifiUser) {
        fireStore.collection(ClassifiStore.USERS).document(user.account?.email.orEmpty())
            .set(user)
    }

    override fun registerUserWithSchool(userId: Long, schoolId: Long) {
        fireStore
            .collection(ClassifiStore.SCHOOL)
            .document(schoolId.toString())
            .collection(ClassifiStore.REGISTRY).document("$userId%%$schoolId")
            .set(ClassifiUserSchoolCrossRef(userId, schoolId))
    }

    override fun unregisterUserWithSchool(userId: Long, schoolId: Long) {
        fireStore
            .collection(ClassifiStore.SCHOOL)
            .document(schoolId.toString())
            .collection(ClassifiStore.REGISTRY)
            .document("$userId%%$schoolId")
            .delete()
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