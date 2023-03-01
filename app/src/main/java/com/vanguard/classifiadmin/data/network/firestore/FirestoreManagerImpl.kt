package com.vanguard.classifiadmin.data.network.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import javax.inject.Inject
import javax.inject.Singleton


object Collections {
    const val collectionUsers = "collection_users"
    const val collectionSchools = "collection_schools"
}


@Singleton
class FirestoreManagerImpl @Inject constructor() : FirestoreManager {
    private val firestore = Firebase.firestore

    override suspend fun saveUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        try {
            firestore.collection(Collections.collectionUsers).document(user.userId ?: "")
                .set(user)
            onResult(true)
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun getUserByIdNetwork(
        userId: String,
        onResult: (Resource<UserNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<UserNetworkModel>())
                    }
                    onResult(Resource.Success(results.first()))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch user")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getUserByEmailNetwork(
        email: String,
        onResult: (Resource<UserNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<UserNetworkModel>())
                    }
                    onResult(Resource.Success(results.first()))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch user")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }


    override suspend fun deleteUserByIdNetwork(userId: String, onResult: (Boolean) -> Unit) {
        try {
            firestore.collection(Collections.collectionUsers).document(userId)
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }

    }

    override suspend fun deleteUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        try {
            firestore.collection(Collections.collectionUsers).document(user.userId ?: "")
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveSchoolNetwork(
        school: SchoolNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(school.schoolId ?: "")
                .set(school)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }

    }

    override suspend fun getSchoolByIdNetwork(
        schoolId: String,
        onResult: (Resource<SchoolNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .get()
                .addOnSuccessListener { doc ->
                    onResult(Resource.Success(doc.toObject<SchoolNetworkModel>()))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch school")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun deleteSchoolByIdNetwork(schoolId: String, onResult: (Boolean) -> Unit) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteSchoolNetwork(
        school: SchoolNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(school.schoolId ?: "")
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }
}