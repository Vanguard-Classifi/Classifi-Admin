package com.vanguard.classifiadmin.data.network.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.SubjectNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import javax.inject.Inject
import javax.inject.Singleton


object Collections {
    const val collectionUsers = "collection_users"
    const val collectionSchools = "collection_schools"
    const val collectionClasses = "collection_classes"
    const val collectionSubjects = "collection_subjects"
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
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    }
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

    override suspend fun saveClassAsStagedNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            myClass.verified = false
            firestore.collection(Collections.collectionSchools).document(myClass.schoolId ?: "")
                .collection(Collections.collectionClasses)
                .document(myClass.classCode ?: "")
                .set(myClass)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveClassesAsStagedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val classCollection = firestore.collection(Collections.collectionSchools)
                .document(myClasses.first().schoolId ?: "")
                .collection(Collections.collectionClasses)

            myClasses.map { myClass ->
                myClass.verified = false
                classCollection.document(myClass.classCode ?: "")
                    .set(myClass)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveClassAsVerifiedNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            myClass.verified = true
            firestore.collection(Collections.collectionSchools).document(myClass.schoolId ?: "")
                .collection(Collections.collectionClasses)
                .document(myClass.classCode ?: "")
                .set(myClass)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveClassesAsVerifiedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val classCollection = firestore.collection(Collections.collectionSchools)
                .document(myClasses.first().schoolId ?: "")
                .collection(Collections.collectionClasses)

            myClasses.map { myClass ->
                myClass.verified = true
                classCollection.document(myClass.classCode ?: "")
                    .set(myClass)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun getClassByIdNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<ClassNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionClasses)
                .whereEqualTo("classId", classId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<ClassNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<ClassNetworkModel>())
                    }
                    onResult(Resource.Success(results.first()))
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun getClassByCodeNetwork(
        code: String,
        schoolId: String,
        onResult: (Resource<ClassNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionClasses)
                .whereEqualTo("classCode", code)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<ClassNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<ClassNetworkModel>())
                    }
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    }
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun getVerifiedClassesNetwork(
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionClasses)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<ClassNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<ClassNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun getStagedClassesNetwork(
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionClasses)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<ClassNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<ClassNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun deleteClassNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(myClass.schoolId ?: "")
                .collection(Collections.collectionClasses)
                .document(myClass.classCode ?: "")
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }

        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteClassesNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val classCollection =
                firestore.collection(Collections.collectionSchools)
                    .document(myClasses.first().schoolId ?: "")
                    .collection(Collections.collectionClasses)
            myClasses.map { myClass ->
                classCollection.document(myClass.classCode ?: "")
                    .delete()
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveSubjectAsStagedNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            subject.verified = false
            firestore.collection(Collections.collectionSchools).document(subject.schoolId ?: "")
                .collection(Collections.collectionSubjects)
                .document(subject.subjectCode ?: "")
                .set(subject)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveSubjectsAsStagedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val subjectCollection = firestore.collection(Collections.collectionSchools)
                .document(subjects.first().schoolId ?: "")
                .collection(Collections.collectionSubjects)

            subjects.map { subject ->
                subject.verified = false
                subjectCollection.document(subject.subjectCode ?: "")
                    .set(subject)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveSubjectAsVerifiedNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            subject.verified = true
            firestore.collection(Collections.collectionSchools).document(subject.schoolId ?: "")
                .collection(Collections.collectionSubjects)
                .document(subject.subjectCode ?: "")
                .set(subject)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveSubjectsAsVerifiedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val subjectCollection = firestore.collection(Collections.collectionSchools)
                .document(subjects.first().schoolId ?: "")
                .collection(Collections.collectionSubjects)

            subjects.map { subject ->
                subject.verified = true
                subjectCollection.document(subject.subjectCode ?: "")
                    .set(subject)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun getSubjectByIdNetwork(
        subjectId: String,
        schoolId: String,
        onResult: (Resource<SubjectNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionSubjects)
                .whereEqualTo("subjectId", subjectId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<SubjectNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<SubjectNetworkModel>())
                    }
                    onResult(Resource.Success(results.first()))
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun getSubjectByCodeNetwork(
        code: String,
        schoolId: String,
        onResult: (Resource<SubjectNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionSubjects)
                .whereEqualTo("subjectCode", code)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<SubjectNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<SubjectNetworkModel>())
                    }
                    if(results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    }
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun getVerifiedSubjectsNetwork(
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionSubjects)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<SubjectNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<SubjectNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun getStagedSubjectsNetwork(
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionSubjects)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<SubjectNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<SubjectNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun deleteSubjectNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(subject.schoolId ?: "")
                .collection(Collections.collectionSubjects)
                .document(subject.subjectCode ?: "")
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }

        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteSubjectsNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val subjectCollection =
                firestore.collection(Collections.collectionSchools)
                    .document(subjects.first().schoolId ?: "")
                    .collection(Collections.collectionSubjects)
            subjects.map { subject ->
                subjectCollection.document(subject.subjectCode ?: "")
                    .delete()
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
        } catch (e: Exception) {
            onResult(false)
        }
    }
}