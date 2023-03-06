package com.vanguard.classifiadmin.data.network.firestore

import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.SubjectNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.domain.helpers.Resource

interface FirestoreManager {
    //user
    suspend fun saveUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit)

    suspend fun saveUserAsVerified(user: UserNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun saveUsersAsVerified(users: List<UserNetworkModel>, onResult: (Boolean) -> Unit)
    suspend fun saveUserToStage(user: UserNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun saveUsersToStage(users: List<UserNetworkModel>, onResult: (Boolean) -> Unit)
    suspend fun getUserByIdNetwork(userId: String, onResult: (Resource<UserNetworkModel?>) -> Unit)
    suspend fun getVerifiedUsersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getStagedUsersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getUserByEmailNetwork(
        email: String,
        onResult: (Resource<UserNetworkModel?>) -> Unit
    )

    suspend fun deleteUserByIdNetwork(userId: String, onResult: (Boolean) -> Unit)
    suspend fun deleteUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit)


    //school
    suspend fun saveSchoolNetwork(school: SchoolNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun getSchoolByIdNetwork(
        schoolId: String,
        onResult: (Resource<SchoolNetworkModel?>) -> Unit
    )

    suspend fun deleteSchoolByIdNetwork(schoolId: String, onResult: (Boolean) -> Unit)
    suspend fun deleteSchoolNetwork(school: SchoolNetworkModel, onResult: (Boolean) -> Unit)


    //class
    suspend fun saveClassAsStagedNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveClassesAsStagedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveClassAsVerifiedNetwork(myClass: ClassNetworkModel, onResult: (Boolean) -> Unit)

    suspend fun saveClassesAsVerifiedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    )

    suspend fun getClassByIdNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<ClassNetworkModel?>) -> Unit
    )

    suspend fun getClassByCodeNetwork(
        code: String,
        schoolId: String,
        onResult: (Resource<ClassNetworkModel?>) -> Unit
    )

    suspend fun getVerifiedClassesNetwork(
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    )

    suspend fun getStagedClassesNetwork(
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    )

    suspend fun deleteClassNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteClassesNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    )


    //subject
    suspend fun saveSubjectAsStagedNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveSubjectsAsStagedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveSubjectAsVerifiedNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveSubjectsAsVerifiedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    )

    suspend fun getSubjectByIdNetwork(
        subjectId: String,
        schoolId: String,
        onResult: (Resource<SubjectNetworkModel?>) -> Unit
    )

    suspend fun getSubjectByCodeNetwork(
        code: String,
        schoolId: String,
        onResult: (Resource<SubjectNetworkModel?>) -> Unit
    )

    suspend fun getVerifiedSubjectsNetwork(
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    )

    suspend fun getStagedSubjectsNetwork(
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    )

    suspend fun deleteSubjectNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteSubjectsNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    )

}