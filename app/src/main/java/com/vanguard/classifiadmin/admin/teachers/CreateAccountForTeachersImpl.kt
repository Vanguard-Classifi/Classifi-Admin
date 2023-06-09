package com.vanguard.classifiadmin.admin.teachers

import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.chatme.network.CreateAccountForTeachers
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.utils.OnCreateAccountState
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StagedUser
import com.khalidtouch.core.common.ClassifiDispatcher
import com.khalidtouch.core.common.Dispatcher
import com.khalidtouch.core.common.firestore.ClassifiStore
import com.vanguard.classifiadmin.admin.base.UserRegistration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateAccountForTeachersImpl @Inject constructor(
    private val userRepository: UserRepository,
    @Dispatcher(ClassifiDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : UserRegistration(), CreateAccountForTeachers {

    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    override fun createAccountForTeachers(
        mySchool: ClassifiSchool?,
        teachers: List<StagedUser>,
        result: OnCreateBatchAccountResult
    ) {
        result(OnCreateAccountState.Starting, listOf())
        val abortedEmails = ArrayList<String>()
        try {
            teachers.forEach { data ->
                val email = checkNotNull(data.user.account?.email)
                authentication.createUserWithEmailAndPassword(
                    email, data.password
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            scope.launch {
                                userRepository.saveUser(data.user)
                                userRepository.registerUserWithSchool(
                                    userId = checkNotNull(data.user.userId),
                                    schoolId = checkNotNull(mySchool?.schoolId),
                                    schoolName = checkNotNull(mySchool?.schoolName)
                                )
                                fireStore.collection(ClassifiStore.USERS).document(email)
                                    .set(data.user).addOnSuccessListener { }
                            }
                        } else {
                            abortedEmails.add(email)
                        }
                    }
            }
            result(OnCreateAccountState.Success, abortedEmails)
        } catch (e: Exception) {
            e.printStackTrace()
            result(OnCreateAccountState.Failed, abortedEmails)
        }
    }
}