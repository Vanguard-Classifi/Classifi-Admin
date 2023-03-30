package com.vanguard.classifiadmin.data.network.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.vanguard.classifiadmin.data.network.models.AssessmentNetworkModel
import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.CommentNetworkModel
import com.vanguard.classifiadmin.data.network.models.FeedNetworkModel
import com.vanguard.classifiadmin.data.network.models.QuestionNetworkModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.SubjectNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.domain.helpers.Resource
import com.vanguard.classifiadmin.domain.helpers.UserRole
import javax.inject.Inject
import javax.inject.Singleton


object Collections {
    const val collectionUsers = "collection_users"
    const val collectionSchools = "collection_schools"
    const val collectionClasses = "collection_classes"
    const val collectionSubjects = "collection_subjects"
    const val collectionFeeds = "collection_feeds"
    const val collectionComments = "collection_comments"
    const val collectionAssessments = "collection_assessments"
    const val collectionQuestions = "collection_questions"
}


@Singleton
class FirestoreManagerImpl @Inject constructor() : FirestoreManager {
    val TAG = "FirestoreManagerImpl"
    private val firestore = Firebase.firestore

    override suspend fun saveUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        try {
            firestore.collection(Collections.collectionUsers).document(user.userId ?: "")
                .set(user)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveUserAsVerified(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        try {
            user.verified = true
            user.password = "xxxxxx"
            firestore.collection(Collections.collectionUsers).document(user.userId ?: "")
                .set(user)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveUsersAsVerified(
        users: List<UserNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val userCollection = firestore.collection(Collections.collectionUsers)
            users.map { user ->
                user.verified = true
                user.password = "xxxxxx"
                userCollection.document(user?.userId ?: "")
                    .set(user)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveUserToStage(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        try {
            user.verified = false
            firestore.collection(Collections.collectionUsers).document(user.userId ?: "")
                .set(user)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveUsersToStage(
        users: List<UserNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val userCollection = firestore.collection(Collections.collectionUsers)
            users.map { user ->
                user.verified = false
                userCollection.document(user?.userId ?: "")
                    .set(user)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
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
                    } else {
                        onResult(Resource.Success(null))
                    }
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch user")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getVerifiedUsersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val user = doc.toObject<UserNetworkModel>()
                        if (user.schoolIds.contains(schoolId)) {
                            results.add(user)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch users")) }
        } catch (e: Exception) {
            onResult(Resource.Error("A problem occurred"))
        }
    }

    override suspend fun getStagedUsersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val user = doc.toObject<UserNetworkModel>()
                        if (user.schoolIds.contains(schoolId)) {
                            results.add(user)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch users")) }
        } catch (e: Exception) {
            onResult(Resource.Error("A problem occurred"))
        }
    }

    override suspend fun getVerifiedTeachersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val teacher = doc.toObject<UserNetworkModel>()
                        if (teacher.schoolIds.contains(schoolId) &&
                            teacher.verified == true &&
                            teacher.roles.contains(UserRole.Teacher.name)
                        ) {
                            //add to container
                            results.add(teacher)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch students")) }
        } catch (e: Exception) {
            onResult(Resource.Error("An error occurred"))
        }
    }

    override suspend fun getStagedTeachersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val teacher = doc.toObject<UserNetworkModel>()
                        if (teacher.schoolIds.contains(schoolId) &&
                            teacher.verified == false &&
                            teacher.roles.contains(UserRole.Teacher.name)
                        ) {
                            //add to container
                            results.add(teacher)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch students")) }
        } catch (e: Exception) {
            onResult(Resource.Error("An error occurred"))
        }
    }

    override suspend fun getVerifiedStudentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val student = doc.toObject<UserNetworkModel>()
                        if (student.schoolIds.contains(schoolId) &&
                            student.verified == true &&
                            student.roles.contains(UserRole.Student.name)
                        ) {
                            //add to container
                            results.add(student)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch students")) }
        } catch (e: Exception) {
            onResult(Resource.Error("An error occurred"))
        }
    }

    override suspend fun getVerifiedStudentsGivenTeacherNetwork(
        teacherId: String,
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionClasses)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<ClassNetworkModel>()
                    val students = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val myClass = doc.toObject<ClassNetworkModel>()
                        if (myClass.teacherIds.contains(teacherId)) {
                            results.add(myClass)
                        }
                    }
                    val userRef = firestore.collection(Collections.collectionUsers)
                    results.map { eachClass ->
                        //collect the student ids
                        eachClass.studentIds.forEach { studentId ->
                            userRef.whereEqualTo("userId", studentId)
                                .get()
                                .addOnSuccessListener { docs ->
                                    val possibleStudents = ArrayList<UserNetworkModel>()
                                    for (doc in docs!!) {
                                        possibleStudents.add(doc.toObject<UserNetworkModel>())
                                    }
                                    if (results.isNotEmpty()) {
                                        students.add(possibleStudents.first())
                                    }
                                }
                        }
                    }
                    onResult(Resource.Success(students))
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun getStagedStudentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val student = doc.toObject<UserNetworkModel>()
                        if (student.schoolIds.contains(schoolId) &&
                            student.verified == false &&
                            student.roles.contains(UserRole.Student.name)
                        ) {
                            //add to container
                            results.add(student)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch students")) }
        } catch (e: Exception) {
            onResult(Resource.Error("An error occurred"))
        }
    }


    override suspend fun getVerifiedParentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val parent = doc.toObject<UserNetworkModel>()
                        if (parent.schoolIds.contains(schoolId) &&
                            parent.verified == true &&
                            parent.roles.contains(UserRole.Parent.name)
                        ) {
                            //add to container
                            results.add(parent)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch students")) }
        } catch (e: Exception) {
            onResult(Resource.Error("An error occurred"))
        }
    }


    override suspend fun getStagedParentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val parent = doc.toObject<UserNetworkModel>()
                        if (parent.schoolIds.contains(schoolId) &&
                            parent.verified == false &&
                            parent.roles.contains(UserRole.Parent.name)
                        ) {
                            //add to container
                            results.add(parent)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch students")) }
        } catch (e: Exception) {
            onResult(Resource.Error("An error occurred"))
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
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    } else {
                        onResult(Resource.Success(null))
                    }
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


    override suspend fun getVerifiedStudentsUnderClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val student = doc.toObject<UserNetworkModel>()
                        if (student.schoolIds.contains(schoolId) &&
                            student.verified == true &&
                            student.roles.contains(UserRole.Student.name) &&
                            student.classIds.contains(classId)
                        ) {
                            //add to container
                            results.add(student)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch students")) }
        } catch (e: Exception) {
            onResult(Resource.Error("An error occurred"))
        }
    }


    override suspend fun getVerifiedTeachersUnderClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionUsers)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<UserNetworkModel>()
                    for (doc in docs!!) {
                        val teacher = doc.toObject<UserNetworkModel>()
                        if (teacher.schoolIds.contains(schoolId) &&
                            teacher.verified == true &&
                            teacher.roles.contains(UserRole.Teacher.name) &&
                            teacher.classIds.contains(classId)
                        ) {
                            //add to container
                            results.add(teacher)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch students")) }
        } catch (e: Exception) {
            onResult(Resource.Error("An error occurred"))
        }
    }

    override suspend fun getVerifiedTeachersUnderSubjectNetwork(
        subject: SubjectNetworkModel,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        try {
            val teacherRef = firestore.collection(Collections.collectionUsers)
            val teachers = ArrayList<UserNetworkModel>()
            if (subject.teacherIds.isNotEmpty()) {
                subject.teacherIds.map { teacherId ->
                    teacherRef.whereEqualTo("userId", teacherId)
                        .get()
                        .addOnSuccessListener { docs ->
                            for (doc in docs!!) {
                                teachers.add(doc.toObject<UserNetworkModel>())
                            }
                            onResult(Resource.Success(teachers))
                        }
                        .addOnFailureListener {
                            onResult(Resource.Error("Couldn't fetch resource"))
                        }
                }
            } else {
                onResult(Resource.Success(listOf()))
            }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
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
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    } else {
                        onResult(Resource.Success(null))
                    }

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
                    } else {
                        onResult(Resource.Success(null))
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

    override suspend fun getVerifiedClassesGivenTeacherNetwork(
        teacherId: String,
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
                        val myClass = doc.toObject<ClassNetworkModel>()
                        if (myClass.teacherIds.contains(teacherId)) {
                            results.add(myClass)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Couldn't fetch resource")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong!"))
        }
    }

    override suspend fun getVerifiedClassesGivenStudentNetwork(
        studentId: String,
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
                        val myClass = doc.toObject<ClassNetworkModel>()
                        if (myClass.studentIds.contains(studentId)) {
                            results.add(myClass)
                        }
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
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    } else {
                        onResult(Resource.Success(null))
                    }
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
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    } else {
                        onResult(Resource.Success(null))
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

    override suspend fun getVerifiedSubjectsGivenTeacherNetwork(
        teacherId: String,
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
                        val subject = doc.toObject<SubjectNetworkModel>()
                        if (subject.teacherIds.contains(teacherId)) {
                            doc.toObject<SubjectNetworkModel>()
                        }
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

    override suspend fun getVerifiedSubjectsUnderClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionSubjects)
                .whereEqualTo("verified", true)
                .whereEqualTo("classId", classId)
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

    override suspend fun saveFeedAsStagedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            feed.verified = false
            firestore.collection(Collections.collectionSchools)
                .document(feed.schoolId.orEmpty())
                .collection(Collections.collectionFeeds)
                .document(feed.feedId.orEmpty())
                .set(feed)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveFeedAsVerifiedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            feed.verified = true
            firestore.collection(Collections.collectionSchools)
                .document(feed.schoolId.orEmpty())
                .collection(Collections.collectionFeeds)
                .document(feed.feedId.orEmpty())
                .set(feed)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun getFeedByIdNetwork(
        feedId: String,
        schoolId: String,
        onResult: (Resource<FeedNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds)
                .whereEqualTo("feedId", feedId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<FeedNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<FeedNetworkModel>())
                    }
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    } else onResult(Resource.Success(null))
                }
                .addOnFailureListener {
                    onResult(Resource.Error("Could not fetch feeds"))
                }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getStagedFeedsNetwork(
        schoolId: String,
        onResult: (Resource<List<FeedNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<FeedNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<FeedNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener {
                    onResult(Resource.Error("Could not fetch feeds"))
                }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getStagedFeedsByClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<FeedNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<FeedNetworkModel>()
                    for (doc in docs!!) {
                        val feed = doc.toObject<FeedNetworkModel>()
                        if (feed.classIds.contains(classId)) {
                            results.add(feed)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener {
                    onResult(Resource.Error("Could not fetch feeds"))
                }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getVerifiedFeedsNetwork(
        schoolId: String,
        onResult: (Resource<List<FeedNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<FeedNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<FeedNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener {
                    onResult(Resource.Error("Could not fetch feeds"))
                }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getVerifiedFeedsByClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<FeedNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<FeedNetworkModel>()
                    for (doc in docs!!) {
                        val feed = doc.toObject<FeedNetworkModel>()
                        if (feed.classIds.contains(classId)) {
                            results.add(feed)
                        }
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener {
                    onResult(Resource.Error("Could not fetch feeds"))
                }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun deleteFeedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools)
                .document(feed.schoolId.orEmpty())
                .collection(Collections.collectionFeeds)
                .document(feed.feedId.orEmpty())
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteFeedByIdNetwork(
        feedId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools)
                .document(schoolId)
                .collection(Collections.collectionFeeds)
                .document(feedId)
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteStagedFeedsNetwork(schoolId: String, onResult: (Boolean) -> Unit) {
        try {
            val feedRef = firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds)
            feedRef.whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<FeedNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<FeedNetworkModel>())
                    }
                    //delete each
                    results.map { stagedFeed ->
                        feedRef.document(stagedFeed.feedId.orEmpty())
                            .delete()
                            .addOnSuccessListener { onResult(true) }
                    }
                }
                .addOnFailureListener {
                    onResult(false)
                }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveCommentNetwork(
        comment: CommentNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(comment.schoolId.orEmpty())
                .collection(Collections.collectionFeeds).document(comment.parentFeedId.orEmpty())
                .collection(Collections.collectionComments)
                .document(comment.commentId.orEmpty())
                .set(comment)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun getCommentByIdNetwork(
        commentId: String,
        feedId: String,
        schoolId: String,
        onResult: (Resource<CommentNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds).document(feedId)
                .collection(Collections.collectionComments)
                .whereEqualTo("commentId", commentId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<CommentNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<CommentNetworkModel>())
                    }
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    } else {
                        onResult(Resource.Success(null))
                    }
                }
                .addOnFailureListener {
                    onResult(Resource.Error("Could not fetch comments"))
                }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getCommentsByFeedNetwork(
        feedId: String,
        schoolId: String,
        onResult: (Resource<List<CommentNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds).document(feedId)
                .collection(Collections.collectionComments)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<CommentNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<CommentNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener {
                    onResult(Resource.Error("Could not fetch comments"))
                }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun deleteCommentNetwork(
        comment: CommentNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(comment.schoolId.orEmpty())
                .collection(Collections.collectionFeeds).document(comment.parentFeedId.orEmpty())
                .collection(Collections.collectionComments)
                .document(comment.commentId.orEmpty())
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteCommentByIdNetwork(
        commentId: String,
        feedId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionFeeds).document(feedId)
                .collection(Collections.collectionComments)
                .document(commentId)
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveAssessmentAsStagedNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            assessment.verified = false
            firestore.collection(Collections.collectionSchools)
                .document(assessment.schoolId.orEmpty())
                .collection(Collections.collectionAssessments)
                .document(assessment.assessmentId.orEmpty())
                .set(assessment)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveAssessmentAsVerifiedNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            assessment.verified = true
            firestore.collection(Collections.collectionSchools)
                .document(assessment.schoolId.orEmpty())
                .collection(Collections.collectionAssessments)
                .document(assessment.assessmentId.orEmpty())
                .set(assessment)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteAssessmentNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools)
                .document(assessment.schoolId.orEmpty())
                .collection(Collections.collectionAssessments)
                .document(assessment.assessmentId.orEmpty())
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteAssessmentByIdNetwork(
        assessmentId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools)
                .document(schoolId)
                .collection(Collections.collectionAssessments)
                .document(assessmentId)
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteStagedAssessmentsByUserNetwork(
        authorId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val assessmentRef = firestore.collection(Collections.collectionSchools)
                .document(schoolId)
                .collection(Collections.collectionAssessments)
            assessmentRef.whereEqualTo("authorId", authorId)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<AssessmentNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<AssessmentNetworkModel>())
                    }
                    results.map { assessment ->
                        assessmentRef.document(assessment.assessmentId.orEmpty())
                            .delete()
                            .addOnSuccessListener { onResult(true) }
                            .addOnFailureListener { onResult(false) }
                    }
                }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun getStagedAssessmentsNetwork(
        authorId: String,
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools)
                .document(schoolId)
                .collection(Collections.collectionAssessments)
                .whereEqualTo("authorId", authorId)
                .whereEqualTo("verified", false)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<AssessmentNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<AssessmentNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch assessments")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getVerifiedAssessmentsNetwork(
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools)
                .document(schoolId)
                .collection(Collections.collectionAssessments)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<AssessmentNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<AssessmentNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch assessments")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getAssessmentByIdNetwork(
        assessmentId: String,
        schoolId: String,
        onResult: (Resource<AssessmentNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools)
                .document(schoolId)
                .collection(Collections.collectionAssessments)
                .whereEqualTo("verified", true)
                .whereEqualTo("assessmentId", assessmentId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<AssessmentNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<AssessmentNetworkModel>())
                    }
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    } else {
                        onResult(Resource.Success(null))
                    }
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch assessments")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun saveQuestionAsStagedNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            question.verified = false
            firestore.collection(Collections.collectionSchools)
                .document(question.schoolId.orEmpty())
                .collection(Collections.collectionQuestions)
                .document(question.questionId.orEmpty())
                .set(question)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun saveQuestionAsVerifiedNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            question.verified = true
            firestore.collection(Collections.collectionSchools)
                .document(question.schoolId.orEmpty())
                .collection(Collections.collectionQuestions)
                .document(question.questionId.orEmpty())
                .set(question)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteQuestionNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools)
                .document(question.schoolId.orEmpty())
                .collection(Collections.collectionQuestions).document(question.questionId.orEmpty())
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteQuestionByIdNetwork(
        questionId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionQuestions).document(questionId)
                .delete()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun deleteStagedQuestionsByUserNetwork(
        authorId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    ) {
        try {
            val questionRef = firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionQuestions)
            questionRef.whereEqualTo("verified", false)
                .whereEqualTo("authorId", authorId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<QuestionNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<QuestionNetworkModel>())
                    }
                    results.map { question ->
                        questionRef.document(question.questionId.orEmpty())
                            .delete()
                            .addOnSuccessListener { onResult(true) }
                            .addOnFailureListener { onResult(false) }
                    }
                }
                .addOnFailureListener { onResult(false) }
        } catch (e: Exception) {
            onResult(false)
        }
    }

    override suspend fun getQuestionByIdNetwork(
        questionId: String,
        schoolId: String,
        onResult: (Resource<QuestionNetworkModel?>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionQuestions)
                .whereEqualTo("questionId", questionId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<QuestionNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<QuestionNetworkModel>())
                    }
                    if (results.isNotEmpty()) {
                        onResult(Resource.Success(results.first()))
                    } else {
                        onResult(Resource.Success(null))
                    }
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch question")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getVerifiedQuestionsNetwork(
        schoolId: String,
        onResult: (Resource<List<QuestionNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionQuestions)
                .whereEqualTo("verified", true)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<QuestionNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<QuestionNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch question")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }

    override suspend fun getStagedQuestionsNetwork(
        schoolId: String,
        authorId: String,
        onResult: (Resource<List<QuestionNetworkModel>>) -> Unit
    ) {
        try {
            firestore.collection(Collections.collectionSchools).document(schoolId)
                .collection(Collections.collectionQuestions)
                .whereEqualTo("verified", false)
                .whereEqualTo("authorId", authorId)
                .get()
                .addOnSuccessListener { docs ->
                    val results = ArrayList<QuestionNetworkModel>()
                    for (doc in docs!!) {
                        results.add(doc.toObject<QuestionNetworkModel>())
                    }
                    onResult(Resource.Success(results))
                }
                .addOnFailureListener { onResult(Resource.Error("Could not fetch question")) }
        } catch (e: Exception) {
            onResult(Resource.Error("Something went wrong"))
        }
    }
}