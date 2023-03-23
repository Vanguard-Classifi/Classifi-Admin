package com.vanguard.classifiadmin.data.network.firestore

import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.CommentNetworkModel
import com.vanguard.classifiadmin.data.network.models.FeedNetworkModel
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

    suspend fun getVerifiedTeachersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getStagedTeachersNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedStudentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getStagedStudentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedParentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getStagedParentsNetwork(
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getUserByEmailNetwork(
        email: String,
        onResult: (Resource<UserNetworkModel?>) -> Unit
    )

    suspend fun deleteUserByIdNetwork(userId: String, onResult: (Boolean) -> Unit)
    suspend fun deleteUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit)

    suspend fun getVerifiedStudentsUnderClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedTeachersUnderClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

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

    suspend fun getVerifiedClassesGivenTeacherNetwork(
        teacherId: String,
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedClassesGivenStudentNetwork(
        studentId: String,
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

    suspend fun getVerifiedSubjectsUnderClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    )

    // feeds
    suspend fun saveFeedAsStagedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveFeedAsVerifiedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    )
    suspend fun getFeedByIdNetwork(
        feedId: String,
        schoolId: String,
        onResult: (Resource<FeedNetworkModel?>) -> Unit
    )

    suspend fun getStagedFeedsNetwork(
        schoolId: String,
        onResult: (Resource<List<FeedNetworkModel>>) -> Unit
    )

    suspend fun getStagedFeedsByClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<FeedNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedFeedsNetwork(
        schoolId: String,
        onResult: (Resource<List<FeedNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedFeedsByClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<FeedNetworkModel>>) -> Unit
    )

    suspend fun deleteFeedNetwork(
        feed: FeedNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteFeedByIdNetwork(
        feedId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteStagedFeedsNetwork(
        schoolId: String,
        onResult: (Boolean) -> Unit
    )

    //comments
    suspend fun saveCommentNetwork(
        comment: CommentNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun getCommentByIdNetwork(
        commentId: String,
        feedId: String,
        schoolId: String,
        onResult: (Resource<CommentNetworkModel?>) -> Unit
    )

    suspend fun getCommentsByFeedNetwork(
        feedId: String,
        schoolId: String,
        onResult: (Resource<List<CommentNetworkModel>>) -> Unit
    )

    suspend fun deleteCommentNetwork(
        comment: CommentNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteCommentByIdNetwork(
        commentId: String,
        feedId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    )

}