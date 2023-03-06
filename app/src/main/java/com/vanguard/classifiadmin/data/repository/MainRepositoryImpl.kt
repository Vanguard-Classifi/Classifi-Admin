package com.vanguard.classifiadmin.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.network.firestore.FirestoreManager
import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.SubjectNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.data.network.storage.FirebaseStorage
import com.vanguard.classifiadmin.domain.auth.FirebaseAuthManager
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val firestoreManager: FirestoreManager,
    private val firebaseStorage: FirebaseStorage,
) : MainRepository {

    override val currentUser: Resource<FirebaseUser?>
        get() = authManager.currentUser

    override fun signUp(
        email: String?,
        password: String?,
        onResult: (Resource<FirebaseUser?>, Resource<AuthExceptionState?>) -> Unit
    ) {
        authManager.signUp(email, password, onResult)
    }

    override fun signIn(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    ) {
        authManager.signIn(email, password, onResult)
    }

    override fun signOut() {
        authManager.signOut()
    }

    override suspend fun saveUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        firestoreManager.saveUserNetwork(user, onResult)
    }

    override suspend fun saveUserAsVerified(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        firestoreManager.saveUserAsVerified(user, onResult)
    }

    override suspend fun saveUsersAsVerified(
        users: List<UserNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveUsersAsVerified(users, onResult)
    }

    override suspend fun saveUserToStage(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        firestoreManager.saveUserToStage(user, onResult)
    }

    override suspend fun saveUsersToStage(
        users: List<UserNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveUsersToStage(users, onResult)
    }

    override suspend fun getUserByIdNetwork(
        userId: String,
        onResult: (Resource<UserNetworkModel?>) -> Unit
    ) {
        firestoreManager.getUserByIdNetwork(userId, onResult)
    }

    override suspend fun getUserByEmailNetwork(
        email: String,
        onResult: (Resource<UserNetworkModel?>) -> Unit
    ) {
        firestoreManager.getUserByEmailNetwork(email, onResult)
    }

    override suspend fun getVerifiedUsersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        firestoreManager.getVerifiedUsersNetwork(schoolId, onResult)
    }

    override suspend fun getStagedUsersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        firestoreManager.getStagedUsersNetwork(schoolId, onResult)
    }

    override suspend fun getVerifiedTeachersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        firestoreManager.getVerifiedTeachersNetwork(
            schoolId, onResult
        )
    }

    override suspend fun getStagedTeachersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        firestoreManager.getStagedTeachersNetwork(schoolId, onResult)
    }

    override suspend fun getVerifiedStudentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
      firestoreManager.getVerifiedStudentsNetwork(schoolId, onResult)
    }

    override suspend fun getStagedStudentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
       firestoreManager.getStagedStudentsNetwork(schoolId, onResult)
    }

    override suspend fun getVerifiedParentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
        firestoreManager.getVerifiedParentsNetwork(schoolId, onResult)
    }

    override suspend fun getStagedParentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    ) {
       firestoreManager.getStagedParentsNetwork(schoolId, onResult)
    }

    override suspend fun deleteUserByIdNetwork(userId: String, onResult: (Boolean) -> Unit) {
        firestoreManager.deleteUserByIdNetwork(userId, onResult)
    }

    override suspend fun deleteUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        firestoreManager.deleteUserNetwork(user, onResult)
    }

    override suspend fun saveSchoolNetwork(
        school: SchoolNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveSchoolNetwork(school, onResult)
    }

    override suspend fun getSchoolByIdNetwork(
        schoolId: String,
        onResult: (Resource<SchoolNetworkModel?>) -> Unit
    ) {
        firestoreManager.getSchoolByIdNetwork(schoolId, onResult)
    }

    override suspend fun deleteSchoolByIdNetwork(schoolId: String, onResult: (Boolean) -> Unit) {
        firestoreManager.deleteSchoolByIdNetwork(schoolId, onResult)
    }

    override suspend fun deleteSchoolNetwork(
        school: SchoolNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.deleteSchoolNetwork(school, onResult)
    }

    override suspend fun uploadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean, String) -> Unit
    ) {
        firebaseStorage.uploadAvatar(fileUri, userId, onProgress, onResult)
    }

    override suspend fun downloadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean, Long) -> Unit
    ) {
        firebaseStorage.downloadAvatar(
            fileUri,
            userId, onProgress, onResult
        )
    }

    override suspend fun saveClassAsStagedNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveClassAsStagedNetwork(
            myClass, onResult
        )
    }

    override suspend fun saveClassesAsStagedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveClassesAsStagedNetwork(myClasses, onResult)
    }

    override suspend fun saveClassAsVerifiedNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveClassAsVerifiedNetwork(myClass, onResult)
    }

    override suspend fun saveClassesAsVerifiedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveClassesAsVerifiedNetwork(myClasses, onResult)
    }


    override suspend fun getClassByIdNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<ClassNetworkModel?>) -> Unit
    ) {
        firestoreManager.getClassByIdNetwork(classId, schoolId, onResult)
    }

    override suspend fun getClassByCodeNetwork(
        code: String,
        schoolId: String,
        onResult: (Resource<ClassNetworkModel?>) -> Unit
    ) {
        firestoreManager.getClassByCodeNetwork(code, schoolId, onResult)
    }

    override suspend fun getVerifiedClassesNetwork(
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    ) {
        firestoreManager.getVerifiedClassesNetwork(schoolId, onResult)
    }

    override suspend fun getStagedClassesNetwork(
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    ) {
        firestoreManager.getStagedClassesNetwork(schoolId, onResult)
    }

    override suspend fun deleteClassNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.deleteClassNetwork(myClass, onResult)
    }

    override suspend fun deleteClassesNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.deleteClassesNetwork(myClasses, onResult)
    }

    override suspend fun saveSubjectAsStagedNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveSubjectAsStagedNetwork(subject, onResult)
    }

    override suspend fun saveSubjectsAsStagedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveSubjectsAsStagedNetwork(subjects, onResult)
    }

    override suspend fun saveSubjectAsVerifiedNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveSubjectAsVerifiedNetwork(subject, onResult)
    }

    override suspend fun saveSubjectsAsVerifiedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveSubjectsAsVerifiedNetwork(subjects, onResult)
    }

    override suspend fun getSubjectByIdNetwork(
        subjectId: String,
        schoolId: String,
        onResult: (Resource<SubjectNetworkModel?>) -> Unit
    ) {
        firestoreManager.getSubjectByIdNetwork(subjectId, schoolId, onResult)
    }

    override suspend fun getSubjectByCodeNetwork(
        code: String,
        schoolId: String,
        onResult: (Resource<SubjectNetworkModel?>) -> Unit
    ) {
        firestoreManager.getSubjectByCodeNetwork(code, schoolId, onResult)
    }

    override suspend fun getVerifiedSubjectsNetwork(
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    ) {
        firestoreManager.getVerifiedSubjectsNetwork(schoolId, onResult)
    }

    override suspend fun getStagedSubjectsNetwork(
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    ) {
        firestoreManager.getStagedSubjectsNetwork(schoolId, onResult)
    }

    override suspend fun deleteSubjectNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.deleteSubjectNetwork(subject, onResult)
    }

    override suspend fun deleteSubjectsNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.deleteSubjectsNetwork(subjects, onResult)
    }
}