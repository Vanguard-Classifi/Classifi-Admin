package com.vanguard.classifiadmin.onboarding

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.chatme.network.CreateAccountForUsers
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.classifiadmin.model.utils.OnCreateAccountState
import com.khalidtouch.classifiadmin.model.utils.OnCreateBatchAccountResult
import com.khalidtouch.classifiadmin.model.utils.StagedUser
import com.khalidtouch.core.common.ClassifiDispatcher
import com.khalidtouch.core.common.Dispatcher
import com.khalidtouch.core.common.firestore.ClassifiStore
import com.vanguard.classifiadmin.onboarding.base.UserRegistration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateAccountForUsersImpl @Inject constructor(
    private val userRepository: UserRepository,
    @Dispatcher(ClassifiDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : UserRegistration(), CreateAccountForUsers {

    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    override fun createAccountForUsers(
        mySchool: ClassifiSchool?,
        users: List<StagedUser>,
        result: OnCreateBatchAccountResult
    ) {
        result(OnCreateAccountState.Starting, listOf())
        val aborted = ArrayList<StagedUser>()
        val success = ArrayList<StagedUser>()
        if (mySchool == null) {
            result(OnCreateAccountState.SchoolNotFound, aborted)
            return
        }
        try {
            users.forEach { data ->
                val email = checkNotNull(data.user.account?.email)
                authentication.createUserWithEmailAndPassword(
                    email, data.password
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            success.add(data)
                        } else {
                            aborted.add(data)
                        }
                    }
            }
            if (success.isEmpty() || aborted.isNotEmpty()) {
                result(OnCreateAccountState.Failed, aborted)
                return
            }
            success.forEach { user ->
                scope.launch {
                    userRepository.saveUser(user.user)
                    userRepository.registerUserWithSchool(
                        userId = checkNotNull(user.user.userId),
                        schoolId = checkNotNull(mySchool.schoolId),
                        schoolName = checkNotNull(mySchool.schoolName)
                    )
                    fireStore.collection(ClassifiStore.USERS)
                        .document(user.user.account?.email.orEmpty())
                        .set(user.user).addOnSuccessListener { }
                }
            }
            result(OnCreateAccountState.Success, success)
        } catch (e: Exception) {
            e.printStackTrace()
            result(OnCreateAccountState.Failed, aborted)
        }
    }
}