package com.vanguard.classifiadmin.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.network.models.AssessmentNetworkModel
import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.CommentNetworkModel
import com.vanguard.classifiadmin.data.network.models.FeedNetworkModel
import com.vanguard.classifiadmin.data.network.models.QuestionNetworkModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.SubjectNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import java.io.File

interface MainRepository {
    val currentUser: Resource<FirebaseUser?>

    fun signUp(
        email: String?,
        password: String?,
        onResult: (Resource<FirebaseUser?>,Resource<AuthExceptionState?>) -> Unit
    )

    fun signIn(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    )

    fun signOut()

    //user
    suspend fun saveUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun saveUserAsVerified(user: UserNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun saveUsersAsVerified(users: List<UserNetworkModel>, onResult: (Boolean) -> Unit)
    suspend fun saveUserToStage(user: UserNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun saveUsersToStage(users: List<UserNetworkModel>, onResult: (Boolean) -> Unit)
    suspend fun getUserByIdNetwork(userId: String, onResult: (Resource<UserNetworkModel?>) -> Unit)
    suspend fun getUserByEmailNetwork(email: String, onResult: (Resource<UserNetworkModel?>) -> Unit)

    suspend fun getVerifiedUsersNetwork(schoolId: String, onResult: (Resource<List<UserNetworkModel>>) -> Unit)
    suspend fun getStagedUsersNetwork(schoolId: String, onResult: (Resource<List<UserNetworkModel>>) -> Unit)

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

    suspend fun getVerifiedTeachersUnderSubjectNetwork(
        subject: SubjectNetworkModel,
        onResult: (Resource<List<UserNetworkModel>>) -> Unit
    )

    //school
    suspend fun saveSchoolNetwork(school: SchoolNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun getSchoolByIdNetwork(schoolId: String, onResult: (Resource<SchoolNetworkModel?>) -> Unit)
    suspend fun deleteSchoolByIdNetwork(schoolId: String, onResult: (Boolean) -> Unit)
    suspend fun deleteSchoolNetwork(school: SchoolNetworkModel, onResult: (Boolean) -> Unit)

    //avatar
    suspend fun uploadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean, String) -> Unit
    )

    suspend fun downloadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean, Long) -> Unit
    )


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

    suspend fun saveSubjectAsVerifiedNetwork(subject: SubjectNetworkModel, onResult: (Boolean) -> Unit)

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

    suspend fun getVerifiedSubjectsGivenTeacherNetwork(
        teacherId: String,
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    )


    // Feed
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

    suspend fun saveImageFile(bitmap: Bitmap, filename: String): File?

    suspend fun saveImageFileAndReturnUri(bitmap: Bitmap): Uri?

    suspend fun saveImageFileAndReturnUri(bitmap: Bitmap, filename: String): Uri?

    suspend fun getUriFromFilename(filename: String): Uri?

    suspend fun getContentType(uri: Uri): String?

    suspend fun getBitmapFromUri(uri: Uri): Bitmap?


    //assessment
    suspend fun saveAssessmentAsStagedNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveAssessmentAsVerifiedNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteAssessmentNetwork(
        assessment: AssessmentNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteAssessmentByIdNetwork(
        assessmentId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteStagedAssessmentsByUserNetwork(
        authorId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    )

    suspend fun getStagedAssessmentsNetwork(
        authorId: String,
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedAssessmentsNetwork(
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedQuestionsByAssessmentNetwork(
        assessmentId: String,
        schoolId: String,
        onResult: (Resource<List<QuestionNetworkModel>>) -> Unit,
    )


    suspend fun getVerifiedAssessmentsDraftNetwork(
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedAssessmentsDraftForClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedAssessmentsInReviewNetwork(
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedAssessmentsInReviewForClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedAssessmentsPublishedNetwork(
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    )

    suspend fun getVerifiedAssessmentsPublishedForClassNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<List<AssessmentNetworkModel>>) -> Unit
    )

    suspend fun getAssessmentByIdNetwork(
        assessmentId: String,
        schoolId: String,
        onResult: (Resource<AssessmentNetworkModel?>) -> Unit
    )

    //question
    suspend fun saveQuestionAsStagedNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit,
    )

    suspend fun saveQuestionAsVerifiedNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit,
    )

    suspend fun deleteQuestionNetwork(
        question: QuestionNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteQuestionByIdNetwork(
        questionId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteStagedQuestionsByUserNetwork(
        authorId: String,
        schoolId: String,
        onResult: (Boolean) -> Unit
    )

    suspend fun getQuestionByIdNetwork(
        questionId: String,
        schoolId: String,
        onResult: (Resource<QuestionNetworkModel?>) -> Unit,
    )

    suspend fun getQuestionByIndexNetwork(
        index: Int,
        assessmentId: String,
        schoolId: String,
        onResult: (Resource<QuestionNetworkModel?>) -> Unit,
    )

    suspend fun getVerifiedQuestionsNetwork(
        schoolId: String,
        onResult: (Resource<List<QuestionNetworkModel>>) -> Unit,
    )

    suspend fun getStagedQuestionsNetwork(
        schoolId: String,
        authorId: String,
        onResult: (Resource<List<QuestionNetworkModel>>) -> Unit,
    )
}