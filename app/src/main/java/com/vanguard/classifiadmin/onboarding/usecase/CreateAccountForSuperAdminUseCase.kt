package com.vanguard.classifiadmin.onboarding.usecase

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.model.UserAccount
import com.khalidtouch.classifiadmin.model.UserRole
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.classifiadmin.model.utils.CreateAccountData
import com.khalidtouch.classifiadmin.model.utils.OnCreateAccountState
import com.khalidtouch.core.common.ClassifiDispatcher
import com.khalidtouch.core.common.Dispatcher
import com.khalidtouch.core.common.extensions.isEmailValid
import com.khalidtouch.core.common.extensions.isPasswordValid
import com.khalidtouch.core.common.firestore.ClassifiStore
import com.vanguard.classifiadmin.onboarding.base.UserRegistration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


class CreateAccountForSuperAdminUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @Dispatcher(ClassifiDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
): UserRegistration() {
    val TAG = "CreateAccount"
    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    operator fun invoke(
        data: CreateAccountData,
        callback: (OnCreateAccountState) -> Unit,
    ) {
        callback(OnCreateAccountState.Starting)
        if (data.email.isBlank()) {
            return callback(OnCreateAccountState.EmptyEmailOrPassword)
        }
        if (data.password.isBlank()) {
            return callback(OnCreateAccountState.EmptyEmailOrPassword)
        }
        if (data.confirmPassword.isBlank() || data.confirmPassword != data.password) {
            return callback(OnCreateAccountState.PasswordNotMatched)
        }

        if (!data.email.isEmailValid()) {
            return callback(OnCreateAccountState.InvalidCredentials)
        }

        if (!data.password.isPasswordValid()) {
            return callback(OnCreateAccountState.InvalidCredentials)
        }

        try {
            Log.e(TAG, "invoke: authentication new user account")
            authentication.createUserWithEmailAndPassword(
                data.email,
                data.password,
            )
                .addOnCompleteListener { task ->
                    //if successful
                    if (task.isSuccessful) {
                        //add user to db
                        scope.launch {
                            userRepository.saveUser(
                                ClassifiUser(
                                    userId = System.currentTimeMillis() + data.email.hashCode(),
                                    account = UserAccount(
                                        email = data.email,
                                        userRole = UserRole.SuperAdmin,
                                    ),
                                    dateCreated = LocalDateTime.now(),
                                )
                            )
                            //todo() - register user to school
                        }.invokeOnCompletion {
                            postAction {
                                scope.launch {
                                    //fetch from db
                                    val currentUser =
                                        userRepository.fetchUserByEmail(data.email) ?: return@launch
                                    Log.e(TAG, "invoke: saved user to db")
                                    //save to firestore
                                    fireStore.collection(ClassifiStore.USERS).document(data.email)
                                        .set(currentUser)
                                        .addOnSuccessListener {
                                        }
                                        .addOnFailureListener {
                                            Log.e(
                                                TAG,
                                                "invoke: error on save user ${it.printStackTrace()}"
                                            )
                                        }

                                    Log.e(TAG, "invoke: saved user to firestore")
                                }
                                Log.e(TAG, "invoke: set user id on protobuf")
                                callback(OnCreateAccountState.Success)
                            }

                        }
                    } else {
                        when (task.exception) {
                            is FirebaseAuthWeakPasswordException -> {
                                return@addOnCompleteListener callback(OnCreateAccountState.WeakPassword)
                            }

                            is FirebaseAuthInvalidUserException -> {
                                return@addOnCompleteListener callback(OnCreateAccountState.InvalidUser)
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                return@addOnCompleteListener callback(OnCreateAccountState.InvalidCredentials)
                            }

                            is FirebaseAuthUserCollisionException -> {
                                return@addOnCompleteListener callback(OnCreateAccountState.UserAlreadyExists)
                            }

                            is FirebaseAuthEmailException -> {
                                return@addOnCompleteListener callback(OnCreateAccountState.EmailNotFound)
                            }

                            is FirebaseNetworkException -> {
                                return@addOnCompleteListener callback(OnCreateAccountState.NetworkProblem)
                            }
                        }
                        callback(OnCreateAccountState.Failed)
                        return@addOnCompleteListener
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "invoke: exception is ${e.printStackTrace()}")
        }
    }
}