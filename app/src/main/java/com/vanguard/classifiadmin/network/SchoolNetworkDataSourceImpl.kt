package com.vanguard.classifiadmin.network

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.network.SchoolNetworkDataSource
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUserSchoolCrossRef
import com.khalidtouch.core.common.firestore.ClassifiStore
import com.vanguard.classifiadmin.workers.KEY_MY_SCHOOL_ID
import com.vanguard.classifiadmin.workers.KEY_SCHOOL_BANNER_FILE_URL
import com.vanguard.classifiadmin.workers.UploadSchoolBannerWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SchoolNetworkDataSourceImpl @Inject constructor(
    private val userDataRepository: UserDataRepository,
    @ApplicationContext val context: Context,
) : SchoolNetworkDataSource {
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
            .set(ClassifiUserSchoolCrossRef(userId, schoolId))
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

    override fun uploadSchoolBannerToCloud(bannerImage: String, schoolId: Long) {
       val workData = Data.Builder()
           .putLong(KEY_MY_SCHOOL_ID, schoolId)
           .putString(KEY_SCHOOL_BANNER_FILE_URL, bannerImage)
           .build()
        val uploadRequest: WorkRequest =
            OneTimeWorkRequestBuilder<UploadSchoolBannerWorker>()
                .setInputData(workData)
                .build()
        WorkManager.getInstance(context).enqueue(uploadRequest)
    }
}