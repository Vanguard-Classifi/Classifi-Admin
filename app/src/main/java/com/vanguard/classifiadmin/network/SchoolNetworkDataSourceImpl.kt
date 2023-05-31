package com.vanguard.classifiadmin.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khalidtouch.chatme.network.SchoolNetworkDataSource
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.core.common.firestore.ClassifiStore
import javax.inject.Inject

class SchoolNetworkDataSourceImpl @Inject constructor() : SchoolNetworkDataSource {
    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun saveSchool(school: ClassifiSchool) {
        firestore.collection(ClassifiStore.SCHOOL).document(school.schoolName.orEmpty())
            .set(school)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    override fun registerUserWithSchool(userId: Long, schoolId: Long, schoolName: String) {
        firestore
            .collection(ClassifiStore.SCHOOL)
            .document(schoolName)
            .collection(ClassifiStore.REGISTRY).document(ClassifiStore.Namespace.UserWithSchool)
            .set(hashMapOf<Long, Long>(userId to schoolId))
    }

    override fun updateSchool(school: ClassifiSchool) {
        firestore.collection(ClassifiStore.SCHOOL).document(school.schoolName.orEmpty())
            .set(school)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    override fun deleteSchool(school: ClassifiSchool) {
        TODO("Not yet implemented")
    }

    override fun fetchSchoolById(schoolId: Long, callback: (ClassifiSchool?) -> Unit) {
        TODO("Not yet implemented")
    }
}